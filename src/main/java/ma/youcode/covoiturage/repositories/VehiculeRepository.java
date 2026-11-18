package ma.youcode.covoiturage.repositories;

import ma.youcode.covoiturage.entities.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {

    boolean existsByImmatriculation(String immatriculation);

    List<Vehicule> findByConducteurId(Long conducteurId);
}
