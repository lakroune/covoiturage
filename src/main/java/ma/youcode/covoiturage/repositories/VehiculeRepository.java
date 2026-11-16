package ma.youcode.covoiturage.repositories;

import ma.youcode.covoiturage.entities.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {
}
