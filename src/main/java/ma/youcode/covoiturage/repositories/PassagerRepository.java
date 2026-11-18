package ma.youcode.covoiturage.repositories;

import java.util.List;

import ma.youcode.covoiturage.entities.Passager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassagerRepository extends JpaRepository<Passager, Long> {

    boolean existsByEmail(String email);

    boolean existsByTelephone(String telephone);

    boolean existsByTelephoneAndIdNot(String telephone, Long id);

    List<Passager> findByEmailContainingIgnoreCase(String email);
}
