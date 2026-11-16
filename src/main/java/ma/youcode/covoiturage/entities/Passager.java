package ma.youcode.covoiturage.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "passagers")
@Getter
@Setter
@NoArgsConstructor
public class Passager extends Utilisateur {

    @OneToMany(mappedBy = "passager")
    private List<Reservation> reservations = new ArrayList<>();

    
}
