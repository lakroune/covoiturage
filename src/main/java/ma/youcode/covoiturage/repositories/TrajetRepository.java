package ma.youcode.covoiturage.repositories;

import ma.youcode.covoiturage.entities.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrajetRepository extends JpaRepository<Trajet, Long> {

    List<Trajet> findByVehiculeConducteurId(Long conducteurId);
}
