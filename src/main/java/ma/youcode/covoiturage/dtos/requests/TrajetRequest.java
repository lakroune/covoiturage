package ma.youcode.covoiturage.dtos.requests;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
    @Min(value = 1, message = "Le nombre de places doit être au minimum de 1")
    @Positive(message = "Le nombre de places doit être positif")
    private Integer placesProposees;

    @NotEmpty(message = "Les points d'arrêt sont obligatoires")
    @Size(min = 2, message = "Un trajet doit contenir au moins deux points d'arrêt")
    private List<@Valid PointArretRequest> pointsArret;
}
