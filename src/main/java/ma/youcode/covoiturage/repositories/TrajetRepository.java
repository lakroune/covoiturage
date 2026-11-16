package ma.youcode.covoiturage.repositories;

import ma.youcode.covoiturage.entities.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrajetRepository extends JpaRepository<Trajet, Long> {
}
