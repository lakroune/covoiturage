package ma.youcode.covoiturage.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

@Service
@RequiredArgsConstructor
public class TrajetServiceImpl implements TrajetService {

    private final TrajetRepository trajetRepository;
    private final PointArretRepository pointArretRepository;
    private final VehiculeRepository vehiculeRepository;
    private final TrajetMapper trajetMapper;
    private final PointArretMapper pointArretMapper;
    private final ReservationMapper reservationMapper;

    @Override
    public TrajetResponse create(TrajetRequest request) {
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

        return trajetMapper.toResponse(trajetRepository.save(trajet));
    }

    private List<PointArretRequest> validateAndSortPoints(List<PointArretRequest> pointsRequest) {
        if (pointsRequest == null || pointsRequest.size() < 2) {
            throw new BusinessException("Un trajet doit contenir au moins deux points d'arret");
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

        return trajetMapper.toResponse(trajet);
    }

    @Override
    public List<TrajetResponse> findByConducteurId(Long conducteurId) {
        return trajetRepository.findByVehiculeConducteurId(conducteurId).stream()
                .map(trajetMapper::toResponse)
                .toList();
    }

    @Override
    public List<PointArretResponse> findPointsArret(Long id) {
        trajetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Trajet introuvable avec l'identifiant : " + id));

        return pointArretRepository.findByTrajetIdOrderByOrdreAsc(id).stream()
                .map(pointArretMapper::toResponse)
                .toList();
    }

    @Override
    public List<ReservationResponse> findReservations(Long id) {
        Trajet trajet = trajetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Trajet introuvable avec l'identifiant : " + id));

        return trajet.getReservations().stream()
                .map(reservationMapper::toResponse)
                .toList();
    }

    @Override
    public Page<TrajetResponse> search(
            String villeDepart,
            String villeArrivee,
            LocalDate date,
            Integer nombrePlaces,
            BigDecimal prixMax,
            Pageable pageable) {
        if (pageable.isPaged() && pageable.getPageSize() <= 0) {
            throw new BusinessException("La page doit être positive et la taille doit être supérieure à 0");
        }

        List<TrajetResponse> resultats = trajetRepository.findAll().stream()
                .filter(trajet -> trajet.getStatut() == StatutTrajet.PUBLIE)
                .filter(trajet -> trajet.getDateDepart().toLocalDate().equals(date))
                .filter(trajet -> hasAvailableSegment(
                        trajet, villeDepart, villeArrivee, nombrePlaces, prixMax))
                .map(trajetMapper::toResponse)
                .toList();

        if (pageable.isUnpaged()) {
            return new PageImpl<>(resultats);
        }

        long debut = pageable.getOffset();
        if (debut >= resultats.size()) {
            return new PageImpl<>(List.of(), pageable, resultats.size());
        }

        int fin = (int) Math.min(debut + pageable.getPageSize(), resultats.size());
        return new PageImpl<>(resultats.subList((int) debut, fin), pageable, resultats.size());
    }

    private boolean hasAvailableSegment(
            Trajet trajet,
            String villeDepart,
            String villeArrivee,
            Integer nombrePlaces,
            BigDecimal prixMax) {

                
        List<PointArret> points = trajet.getPointsArret().stream()
                .sorted(Comparator.comparing(PointArret::getOrdre))
                .toList();

        Map<Long, PointArret> pointsById = points.stream()
                .collect(Collectors.toMap(PointArret::getId, Function.identity()));

        for (PointArret depart : points) {
            if (!depart.getVille().equalsIgnoreCase(villeDepart)) {
                continue;
            }

            for (PointArret arrivee : points) {
                if (!arrivee.getVille().equalsIgnoreCase(villeArrivee)
                        || depart.getOrdre() >= arrivee.getOrdre()) {
                    continue;
                }

                BigDecimal prixSegment = arrivee.getPrixDepuisDepart()
                        .subtract(depart.getPrixDepuisDepart());

                int placesReservees = trajet.getReservations().stream()
                        .filter(reservation -> reservation.getStatut() == StatutReservation.CONFIRMEE)
                        .filter(reservation -> {
                            PointArret reservationDepart = pointsById.get(reservation.getPointMonteeId());
                            PointArret reservationArrivee = pointsById.get(reservation.getPointDescenteId());
                            return reservationDepart != null
                                    && reservationArrivee != null
                                    && reservationDepart.getOrdre() < arrivee.getOrdre()
                                    && reservationArrivee.getOrdre() > depart.getOrdre();
                        })
                        .mapToInt(reservation -> reservation.getNombrePlaces())
                        .sum();

                int placesDisponibles = trajet.getPlacesProposees() - placesReservees;
                if (placesDisponibles >= nombrePlaces && prixSegment.compareTo(prixMax) <= 0) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public TrajetResponse terminer(Long id) {
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

        trajet.setStatut(StatutTrajet.TERMINE);
        return trajetMapper.toResponse(trajetRepository.save(trajet));
    }

    @Override
    public TrajetResponse annuler(Long id) {
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

        trajet.setStatut(StatutTrajet.ANNULE);
        trajet.getReservations().forEach(
                reservation -> reservation.setStatut(StatutReservation.ANNULEE));

        return trajetMapper.toResponse(trajetRepository.save(trajet));
    }
}
