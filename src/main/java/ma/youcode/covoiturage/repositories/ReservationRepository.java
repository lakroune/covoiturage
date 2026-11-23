package ma.youcode.covoiturage.repositories;

import ma.youcode.covoiturage.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByTrajetIdAndPassagerId(Long trajetId, Long passagerId);
}
