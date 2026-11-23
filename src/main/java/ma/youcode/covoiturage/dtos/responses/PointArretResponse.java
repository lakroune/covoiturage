package ma.youcode.covoiturage.dtos.responses;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PointArretResponse {

    private Long id;
    private String ville;
    private Integer ordre;
    private LocalDateTime heurePassage;
    private Double prixDepuisDepart;
    private Long trajetId;
}
