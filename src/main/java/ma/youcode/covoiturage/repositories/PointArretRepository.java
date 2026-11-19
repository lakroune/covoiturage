package ma.youcode.covoiturage.repositories;

import ma.youcode.covoiturage.entities.PointArret;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointArretRepository extends JpaRepository<PointArret, Long> {

    List<PointArret> findByTrajetIdOrderByOrdreAsc(Long trajetId);
}
