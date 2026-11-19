package ma.youcode.covoiturage.dtos.requests;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PointArretRequest {

    @NotBlank(message = "La ville est obligatoire")
    private String ville;

    @NotNull(message = "L'ordre est obligatoire")
    @PositiveOrZero(message = "L'ordre doit être positif ou nul")
    private Integer ordre;

    @NotNull(message = "L'heure de passage est obligatoire")
    @FutureOrPresent(message = "L'heure de passage doit être future ou présente")
    private LocalDateTime heurePassage;

    @NotNull(message = "Le prix depuis le départ est obligatoire")
    @DecimalMin(value = "0.0", inclusive = true, message = "Le prix doit être positif ou nul")
    private BigDecimal prixDepuisDepart;
}
