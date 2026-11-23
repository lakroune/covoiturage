package ma.youcode.covoiturage.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.youcode.covoiturage.dtos.requests.ReservationRequest;
import ma.youcode.covoiturage.dtos.responses.ReservationResponse;
import ma.youcode.covoiturage.entities.Passager;
import ma.youcode.covoiturage.entities.PointArret;
import ma.youcode.covoiturage.entities.Reservation;
import ma.youcode.covoiturage.entities.Trajet;
import ma.youcode.covoiturage.enums.StatutReservation;
import ma.youcode.covoiturage.enums.StatutTrajet;
import ma.youcode.covoiturage.exceptions.BusinessException;
import ma.youcode.covoiturage.exceptions.ConflictException;
import ma.youcode.covoiturage.exceptions.ResourceNotFoundException;
import ma.youcode.covoiturage.mappers.ReservationMapper;
import ma.youcode.covoiturage.repositories.PassagerRepository;
import ma.youcode.covoiturage.repositories.PointArretRepository;
import ma.youcode.covoiturage.repositories.ReservationRepository;
import ma.youcode.covoiturage.repositories.TrajetRepository;
import ma.youcode.covoiturage.services.ReservationService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final PassagerRepository passagerRepository;
    private final TrajetRepository trajetRepository;
    private final PointArretRepository pointArretRepository;
    private final ReservationMapper reservationMapper;

    @Override
    public ReservationResponse create(ReservationRequest request) {
        log.info("Creation d'une reservation en cours, passagerId={}, trajetId={}",
                request.getPassagerId(), request.getTrajetId());

        
        Passager passager = passagerRepository.findById(request.getPassagerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Passager introuvable avec l'identifiant : " + request.getPassagerId()));




        Trajet trajet = trajetRepository.findById(request.getTrajetId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Trajet introuvable avec l'identifiant : " + request.getTrajetId()));

        if (reservationRepository.existsByTrajetIdAndPassagerId(
                request.getTrajetId(), request.getPassagerId())) {
            throw new ConflictException(
                    "Le passager possède déjà une réservation pour ce trajet.");
        }


        if (trajet.getStatut() != StatutTrajet.PUBLIE) {
            throw new ConflictException("Ce trajet n'est plus disponible pour une réservation.");
        }


        PointArret pointMontee = findPoint(request.getPointMonteeId());
        PointArret pointDescente = findPoint(request.getPointDescenteId());

        if (!trajet.getId().equals(pointMontee.getTrajet().getId())
                || !trajet.getId().equals(pointDescente.getTrajet().getId())) {
            throw new BusinessException("Les points d'arrêt doivent appartenir au même trajet.");
        }

        if (pointMontee.getOrdre() >= pointDescente.getOrdre()) {
            throw new BusinessException(
                    "Le point de montée doit précéder le point de descente.");
        }

        int placesReservees = trajet.getReservations().stream()
                .filter(reservation -> reservation.getStatut() == StatutReservation.CONFIRMEE)
                .filter(reservation -> reservationChevauche(
                        reservation, pointMontee.getOrdre(), pointDescente.getOrdre()))
                .mapToInt(Reservation::getNombrePlaces)
                .sum();

        int placesRestantes = trajet.getPlacesProposees() - placesReservees;
        if (request.getNombrePlaces() > placesRestantes) {
            throw new ConflictException(
                    "Nombre de places insuffisant. Places restantes : " + placesRestantes);
        }

        Reservation reservation = reservationMapper.toEntity(request);
        reservation.setPointMontee(pointMontee);
        reservation.setPointDescente(pointDescente);
        reservation.setPassager(passager);
        reservation.setTrajet(trajet);
        double prixSegment = pointDescente.getPrixDepuisDepart().doubleValue()
                - pointMontee.getPrixDepuisDepart().doubleValue();
        reservation.setMontantTotal(java.math.BigDecimal.valueOf(
                prixSegment * request.getNombrePlaces()));
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setStatut(StatutReservation.CONFIRMEE);

        Reservation savedReservation = reservationRepository.save(reservation);
        log.info("Reservation creee avec succes, id={}", savedReservation.getId());
        return reservationMapper.toResponse(savedReservation);
    }

    private PointArret findPoint(Long id) {
        return pointArretRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Point d'arrêt introuvable avec l'identifiant : " + id));
    }

    private boolean reservationChevauche(
            Reservation reservation, int ordreMontee, int ordreDescente) {
        PointArret reservationMontee = reservation.getPointMontee();
        PointArret reservationDescente = reservation.getPointDescente();

        return reservationMontee.getOrdre() < ordreDescente
                && reservationDescente.getOrdre() > ordreMontee;
    }



    
    @Override
    public ReservationResponse findById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reservation introuvable avec l'identifiant : " + id));

        log.info("Reservation recuperee, id={}", id);
        return reservationMapper.toResponse(reservation);
    }

    @Override
    public List<ReservationResponse> findByPassagerId(Long passagerId) {
        Passager passager = passagerRepository.findById(passagerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Passager introuvable avec l'identifiant : " + passagerId));

        List<ReservationResponse> reservations = passager.getReservations().stream()
                .map(reservationMapper::toResponse)
                .toList();
        log.info("Reservations du passager recuperees, passagerId={}, nombre={}",
                passagerId, reservations.size());
        return reservations;
    }

    @Override
    public ReservationResponse annuler(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reservation introuvable avec l'identifiant : " + id));

        if (reservation.getStatut() == StatutReservation.ANNULEE) {
            throw new ConflictException("La reservation est déjà annulée");
        }

        LocalDateTime dateLimiteAnnulation = reservation.getTrajet().getDateDepart().minusHours(24);
        if (LocalDateTime.now().isAfter(dateLimiteAnnulation)) {
            throw new ConflictException(
                    "Une réservation ne peut être annulée que jusqu'à 24 heures avant le départ.");
        }

        reservation.setStatut(StatutReservation.ANNULEE);
        Reservation cancelledReservation = reservationRepository.save(reservation);
        log.info("Reservation annulee avec succes, id={}", id);
        return reservationMapper.toResponse(cancelledReservation);
    }
}
