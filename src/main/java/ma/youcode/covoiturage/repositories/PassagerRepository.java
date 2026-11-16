package ma.youcode.covoiturage.repositories;

import ma.youcode.covoiturage.entities.Passager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassagerRepository extends JpaRepository<Passager, Long> {
}
