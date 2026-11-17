package ma.youcode.covoiturage.dtos.responses;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.youcode.covoiturage.enums.StatutTrajet;

@Getter
@Setter
@NoArgsConstructor
public class TrajetResponse {

    private Long id;
    private String villeDepart;
    private String villeArrivee;
    private LocalDateTime dateDepart;
    private Integer placesProposees;
    private StatutTrajet statut;
    private Long conducteurId;
    private Long vehiculeId;
    private List<PointArretResponse> pointsArret;
}
