package ma.youcode.covoiturage.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationRequest {

    @NotNull(message = "Le nombre de places est obligatoire")
    @Positive(message = "Le nombre de places doit être positif")
    private Integer nombrePlaces;

    @NotNull(message = "Le passager est obligatoire")
    @Positive(message = "L'identifiant du passager doit être positif")
    private Long passagerId;

    @NotNull(message = "Le trajet est obligatoire")
    @Positive(message = "L'identifiant du trajet doit être positif")
    private Long trajetId;

    @NotNull(message = "Le point de montée est obligatoire")
    @Positive(message = "L'identifiant du point de montée doit être positif")
    private Long pointMonteeId;

    @NotNull(message = "Le point de descente est obligatoire")
    @Positive(message = "L'identifiant du point de descente doit être positif")
    private Long pointDescenteId;
}
