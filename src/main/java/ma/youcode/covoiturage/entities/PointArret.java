package ma.youcode.covoiturage.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "points_arret")
@Getter
@Setter
@NoArgsConstructor
public class PointArret {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ville;

    @Column(nullable = false)
    private Integer ordre;

    @Column(nullable = false)
    private LocalDateTime heurePassage;

    @Column(nullable = false)
    private BigDecimal prixDepuisDepart;

    @ManyToOne
    @JoinColumn(name = "trajet_id", nullable = false)
    private Trajet trajet;

    @OneToMany(mappedBy = "pointMontee")
    private List<Reservation> reservationsPointMontee;

    @OneToMany(mappedBy = "pointDescente")
    private List<Reservation> reservationsPointDescente;

}
