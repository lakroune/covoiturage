package ma.youcode.covoiturage.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ma.youcode.covoiturage.dtos.requests.PointArretRequest;
import ma.youcode.covoiturage.dtos.requests.TrajetRequest;
import ma.youcode.covoiturage.dtos.responses.PointArretResponse;
import ma.youcode.covoiturage.dtos.responses.ReservationResponse;
import ma.youcode.covoiturage.dtos.responses.TrajetResponse;
import ma.youcode.covoiturage.entities.PointArret;
import ma.youcode.covoiturage.entities.Trajet;
import ma.youcode.covoiturage.entities.Vehicule;
import ma.youcode.covoiturage.enums.StatutTrajet;
import ma.youcode.covoiturage.enums.StatutReservation;
import ma.youcode.covoiturage.exceptions.BusinessException;
import ma.youcode.covoiturage.exceptions.ConflictException;
import ma.youcode.covoiturage.exceptions.ResourceNotFoundException;
import ma.youcode.covoiturage.mappers.TrajetMapper;
import ma.youcode.covoiturage.mappers.PointArretMapper;
import ma.youcode.covoiturage.mappers.ReservationMapper;
import ma.youcode.covoiturage.repositories.PointArretRepository;
import ma.youcode.covoiturage.repositories.TrajetRepository;
import ma.youcode.covoiturage.repositories.VehiculeRepository;
import ma.youcode.covoiturage.services.TrajetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrajetServiceImpl implements TrajetService {

    private final TrajetRepository trajetRepository;
    private final PointArretRepository pointArretRepository;
    private final VehiculeRepository vehiculeRepository;
    private final TrajetMapper trajetMapper;
    private final PointArretMapper pointArretMapper;
    private final ReservationMapper reservationMapper;

    @Override
    public TrajetResponse create(TrajetRequest request) {
        log.info("Creation d'un trajet en cours, vehiculeId={}", request.getVehiculeId());
        Vehicule vehicule = vehiculeRepository.findById(request.getVehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vehicule introuvable avec l'identifiant : " + request.getVehiculeId()));

        if (request.getPlacesProposees() > vehicule.getCapacite()) {
            throw new BusinessException(
                    "Le nombre de places proposées ne peut pas dépasser la capacité du véhicule : "
                            + vehicule.getCapacite());
        }

        List<PointArretRequest> pointsRequest = validateAndSortPoints(request.getPointsArret());

        Trajet trajet = trajetMapper.toEntity(request);
        trajet.setVehicule(vehicule);
        trajet.setStatut(StatutTrajet.PUBLIE);

        List<PointArret> pointsArret = new ArrayList<>();
        for (PointArretRequest pointRequest : pointsRequest) {
            PointArret pointArret = new PointArret();
            pointArret.setVille(pointRequest.getVille());
            pointArret.setOrdre(pointRequest.getOrdre());
            pointArret.setHeurePassage(pointRequest.getHeurePassage());
            pointArret.setPrixDepuisDepart(pointRequest.getPrixDepuisDepart());
            pointArret.setTrajet(trajet);
            pointsArret.add(pointArret);
        }
        trajet.setDateDepart(pointsArret.stream()
                .min(Comparator.comparing(PointArret::getOrdre))
                .orElseThrow(() -> new BusinessException("Au moins un point d'arrêt est requis"))
                .getHeurePassage());
        trajet.setPointsArret(pointsArret);

        Trajet savedTrajet = trajetRepository.save(trajet);
        log.info("Trajet cree avec succes, id={}", savedTrajet.getId());
        return trajetMapper.toResponse(savedTrajet);
    }

    private List<PointArretRequest> validateAndSortPoints(List<PointArretRequest> pointsRequest) {
        if (pointsRequest == null || pointsRequest.size() < 2) {
            throw new BusinessException("Un trajet doit contenir au moins deux points d'arret");
        }

        Set<String> villes = new HashSet<>();
        for (PointArretRequest pointRequest : pointsRequest) {
            if (pointRequest.getVille() == null
                    || !villes.add(pointRequest.getVille().trim().toLowerCase())) {
                throw new BusinessException(
                        "Une ville ne peut apparaître qu'une seule fois sur le parcours");
            }
        }

        List<PointArretRequest> pointsTries = pointsRequest.stream()
                .sorted(Comparator.comparing(PointArretRequest::getOrdre))
                .toList();

        if (pointsTries.get(0).getOrdre() != 0) {
            throw new BusinessException("Le premier point d'arret doit avoir l'ordre 0");
        }

        if (pointsTries.get(0).getPrixDepuisDepart().compareTo(BigDecimal.ZERO) != 0) {
            throw new BusinessException("Le prix du premier point doit etre egal a 0");
        }

        for (int index = 0; index < pointsTries.size(); index++) {
            PointArretRequest point = pointsTries.get(index);
            if (point.getOrdre() != index) {
                throw new BusinessException("Les ordres des points d'arret doivent etre consecutifs");
            }

            if (index > 0) {
                PointArretRequest precedent = pointsTries.get(index - 1);
                if (!point.getHeurePassage().isAfter(precedent.getHeurePassage())) {
                    throw new BusinessException("Les heures de passage doivent etre croissantes de point arret "+point.getVille());
                }
                if (point.getPrixDepuisDepart().compareTo(precedent.getPrixDepuisDepart()) <= 0) {
                    throw new BusinessException("Les prix depuis le depart doivent etre croissants de point arret "+point.getVille());
                }
            }
        }

        return pointsTries;
    }

    @Override
    public TrajetResponse findById(Long id) {
        Trajet trajet = trajetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Trajet introuvable avec l'identifiant : " + id));

        log.info("Trajet recupere, id={}", id);
        return trajetMapper.toResponse(trajet);
    }

    @Override
    public List<TrajetResponse> findByConducteurId(Long conducteurId) {
        List<TrajetResponse> trajets = trajetRepository.findByVehiculeConducteurId(conducteurId).stream()
                .map(trajetMapper::toResponse)
                .toList();
        log.info("Trajets du conducteur recuperes, conducteurId={}, nombre={}", conducteurId, trajets.size());
        return trajets;
    }

    @Override
    public List<PointArretResponse> findPointsArret(Long id) {
        trajetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Trajet introuvable avec l'identifiant : " + id));

        List<PointArretResponse> points = pointArretRepository.findByTrajetIdOrderByOrdreAsc(id).stream()
                .map(pointArretMapper::toResponse)
                .toList();
        log.info("Points d'arret recuperes, trajetId={}, nombre={}", id, points.size());
        return points;
    }

    @Override
    public List<ReservationResponse> findReservations(Long id) {
        Trajet trajet = trajetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Trajet introuvable avec l'identifiant : " + id));

        List<ReservationResponse> reservations = trajet.getReservations().stream()
                .map(reservationMapper::toResponse)
                .toList();
        log.info("Reservations du trajet recuperees, trajetId={}, nombre={}", id, reservations.size());
        return reservations;
    }

    @Override
    public Page<TrajetResponse> search(
            String villeDepart,
            String villeArrivee,
            LocalDate date,
            Integer nombrePlaces,
            BigDecimal prixMax,
            Pageable pageable) {
        log.info("Recherche de trajets, depart={}, arrivee={}, date={}", villeDepart, villeArrivee, date);
        if (pageable.isPaged() && pageable.getPageSize() <= 0) {
            throw new BusinessException("La page doit être positive et la taille doit être supérieure à 0");
        }

        LocalDateTime dateDebut = date.atStartOfDay();
        LocalDateTime dateFin = date.plusDays(1).atStartOfDay();

        Page<TrajetResponse> resultats = trajetRepository.searchAvailableSegments(
                        villeDepart,
                        villeArrivee,
                        dateDebut,
                        dateFin,
                        nombrePlaces,
                        prixMax,
                        StatutTrajet.PUBLIE,
                        StatutReservation.CONFIRMEE,
                        pageable)
                .map(trajetMapper::toResponse);
        log.info("Recherche de trajets terminee, nombre={}", resultats.getNumberOfElements());
        return resultats;
    }

    @Override
    public TrajetResponse terminer(Long id) {
        log.info("Finalisation du trajet en cours, id={}", id);
        Trajet trajet = trajetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Trajet introuvable avec l'identifiant : " + id));

        if (trajet.getStatut() == StatutTrajet.TERMINE) {
            throw new ConflictException("Le trajet est déjà terminé");
        }

        if (trajet.getStatut() != StatutTrajet.PUBLIE) {
            throw new ConflictException(
                    "Seul un trajet publié peut être terminé");
        }

        if (LocalDateTime.now().isBefore(trajet.getDateDepart())) {
            throw new ConflictException(
                    "Un trajet ne peut être terminé qu'après son heure de départ.");
        }

        trajet.setStatut(StatutTrajet.TERMINE);
        Trajet trajetTermine = trajetRepository.save(trajet);
        log.info("Trajet termine avec succes, id={}", id);
        return trajetMapper.toResponse(trajetTermine);
    }

    @Override
    @Transactional
    public TrajetResponse annuler(Long id) {
        log.info("Annulation du trajet en cours, id={}", id);
        Trajet trajet = trajetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Trajet introuvable avec l'identifiant : " + id));

        if (trajet.getStatut() == StatutTrajet.ANNULE) {
            throw new ConflictException("Le trajet est déjà annulé");
        }

        if (trajet.getStatut() != StatutTrajet.PUBLIE) {
            throw new ConflictException(
                    "Seul un trajet publié peut être annulé");
        }

        if (!LocalDateTime.now().isBefore(trajet.getDateDepart())) {
            throw new ConflictException(
                    "Un trajet ne peut être annulé qu'avant son heure de départ.");
        }

        trajet.setStatut(StatutTrajet.ANNULE);
        trajet.getReservations().forEach(
                reservation -> reservation.setStatut(StatutReservation.ANNULEE));

        Trajet trajetAnnule = trajetRepository.save(trajet);
        log.info("Trajet annule avec succes, id={}", id);
        return trajetMapper.toResponse(trajetAnnule);
    }
}
