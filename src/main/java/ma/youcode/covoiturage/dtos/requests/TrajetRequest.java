package ma.youcode.covoiturage.dtos.requests;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrajetRequest {

    @NotNull(message = "La date de départ est obligatoire")
    @Future(message = "La date de départ doit être future")
    private LocalDateTime dateDepart;

    @NotNull(message = "Le nombre de places est obligatoire")
    @Positive(message = "Le nombre de places doit être positif")
    private Integer placesProposees;

    @NotNull(message = "Le véhicule est obligatoire")
    private Long vehiculeId;
}
