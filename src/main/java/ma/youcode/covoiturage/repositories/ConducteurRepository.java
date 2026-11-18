package ma.youcode.covoiturage.repositories;

import ma.youcode.covoiturage.entities.Conducteur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConducteurRepository extends JpaRepository<Conducteur, Long> {

    boolean existsByEmail(String email);

    boolean existsByNumeroPermis(String numeroPermis);

    List<Conducteur> findByEmailContainingIgnoreCase(String email);

    List<Conducteur> findByNomContainingIgnoreCase(String nom);
}
