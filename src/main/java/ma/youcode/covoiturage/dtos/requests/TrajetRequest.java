package ma.youcode.covoiturage.dtos.requests;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrajetRequest {

    @NotBlank(message = "La ville de départ est obligatoire")
    private String villeDepart;

    @NotBlank(message = "La ville d'arrivée est obligatoire")
    private String villeArrivee;

    @NotNull(message = "La date de départ est obligatoire")
    @Future(message = "La date de départ doit être future")
    private LocalDateTime dateDepart;

    @NotNull(message = "Le nombre de places est obligatoire")
    @Positive(message = "Le nombre de places doit être positif")
    private Integer placesProposees;

    @NotNull(message = "Le conducteur est obligatoire")
    private Long conducteurId;

    @NotNull(message = "Le véhicule est obligatoire")
    private Long vehiculeId;
}
