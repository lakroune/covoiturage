package ma.youcode.covoiturage.dtos.responses;

import java.math.BigDecimal;
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
    private BigDecimal prixDepuisDepart;
    private Long trajetId;
}
