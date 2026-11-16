package ma.youcode.covoiturage.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "conducteurs")
@Getter
@Setter
@NoArgsConstructor
public class Conducteur extends Utilisateur {

    private String numeroPermis;

    @OneToMany(mappedBy = "conducteur", fetch = FetchType.LAZY)
    private List<Vehicule> vehicules = new ArrayList<>();

    @OneToMany(mappedBy = "conducteur", fetch = FetchType.LAZY)
    private List<Trajet> trajets = new ArrayList<>();
}
