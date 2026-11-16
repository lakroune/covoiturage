package ma.youcode.covoiturage.repositories;

import ma.youcode.covoiturage.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
}
