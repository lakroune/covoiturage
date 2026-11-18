package ma.youcode.covoiturage.repositories;

import ma.youcode.covoiturage.entities.Conducteur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConducteurRepository extends JpaRepository<Conducteur, Long> {

    boolean existsByEmail(String email);

    boolean existsByNumeroPermis(String numeroPermis);

    Optional<Conducteur> findByEmail(String email);

    List<Conducteur> findByNomContainingIgnoreCase(String nom);
}
