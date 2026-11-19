package ma.youcode.covoiturage.dtos.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VehiculeRequestUpdate {

    @NotBlank(message = "La marque est obligatoire")
    private String marque;

    @NotBlank(message = "Le modèle est obligatoire")
    private String modele;

    @NotBlank(message = "L'immatriculation est obligatoire")
    private String immatriculation;

    @NotBlank(message = "La couleur est obligatoire")
    private String couleur;

    @NotNull(message = "La capacité est obligatoire")
    @Min(value = 1, message = "La capacité doit être au minimum de 1 place")
    @Max(value = 8, message = "La capacité doit être au maximum de 8 places")
    private Integer capacite;
}
