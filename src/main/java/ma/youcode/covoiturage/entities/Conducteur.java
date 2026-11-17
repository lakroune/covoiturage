package ma.youcode.covoiturage.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
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

    @OneToMany(mappedBy = "conducteur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vehicule> vehicules = new ArrayList<>();

}
