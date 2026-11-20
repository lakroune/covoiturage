package ma.youcode.covoiturage.repositories;

import ma.youcode.covoiturage.entities.Trajet;
import ma.youcode.covoiturage.enums.StatutReservation;
import ma.youcode.covoiturage.enums.StatutTrajet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TrajetRepository extends JpaRepository<Trajet, Long> {

    List<Trajet> findByVehiculeConducteurId(Long conducteurId);

    @Query(value = """
            SELECT DISTINCT t
            FROM Trajet t
            JOIN t.pointsArret depart
            JOIN t.pointsArret arrivee
            WHERE t.statut = :statutTrajet
              AND t.dateDepart >= :dateDebut
              AND t.dateDepart < :dateFin
              AND LOWER(depart.ville) = LOWER(:villeDepart)
              AND LOWER(arrivee.ville) = LOWER(:villeArrivee)
              AND depart.ordre < arrivee.ordre
              AND arrivee.prixDepuisDepart - depart.prixDepuisDepart <= :prixMax
              AND t.placesProposees >= :nombrePlaces + COALESCE(
                    (SELECT SUM(reservation.nombrePlaces)
                     FROM Reservation reservation
                     JOIN reservation.pointMontee reservationDepart
                     JOIN reservation.pointDescente reservationArrivee
                     WHERE reservation.trajet = t
                       AND reservation.statut = :statutReservation
                       AND reservationDepart.ordre < arrivee.ordre
                       AND reservationArrivee.ordre > depart.ordre), 0)
            """,
            countQuery = """
            SELECT COUNT(DISTINCT t.id)
            FROM Trajet t
            JOIN t.pointsArret depart
            JOIN t.pointsArret arrivee
            WHERE t.statut = :statutTrajet
              AND t.dateDepart >= :dateDebut
              AND t.dateDepart < :dateFin
              AND LOWER(depart.ville) = LOWER(:villeDepart)
              AND LOWER(arrivee.ville) = LOWER(:villeArrivee)
              AND depart.ordre < arrivee.ordre
              AND arrivee.prixDepuisDepart - depart.prixDepuisDepart <= :prixMax
              AND t.placesProposees >= :nombrePlaces + COALESCE(
                    (SELECT SUM(reservation.nombrePlaces)
                     FROM Reservation reservation
                     JOIN reservation.pointMontee reservationDepart
                     JOIN reservation.pointDescente reservationArrivee
                     WHERE reservation.trajet = t
                       AND reservation.statut = :statutReservation
                       AND reservationDepart.ordre < arrivee.ordre
                       AND reservationArrivee.ordre > depart.ordre), 0)
            """)
    Page<Trajet> searchAvailableSegments(
            @Param("villeDepart") String villeDepart,
            @Param("villeArrivee") String villeArrivee,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin,
            @Param("nombrePlaces") Integer nombrePlaces,
            @Param("prixMax") BigDecimal prixMax,
            @Param("statutTrajet") StatutTrajet statutTrajet,
            @Param("statutReservation") StatutReservation statutReservation,
            Pageable pageable);
}
