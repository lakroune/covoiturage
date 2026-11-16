package ma.youcode.covoiturage.repositories;

import ma.youcode.covoiturage.entities.PointArret;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointArretRepository extends JpaRepository<PointArret, Long> {
}
