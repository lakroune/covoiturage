package ma.youcode.covoiturage.dtos.requests;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrajetRequest {

    @NotNull(message = "Le véhicule est obligatoire")
    private Long vehiculeId;

    @NotNull(message = "Le nombre de places est obligatoire")
    @Positive(message = "Le nombre de places doit être positif")
    private Integer placesProposees;

    @NotEmpty(message = "Les points d'arrêt sont obligatoires")
    private List<@Valid PointArretRequest> pointsArret;
}
