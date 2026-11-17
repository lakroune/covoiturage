package ma.youcode.covoiturage.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VehiculeRequest {

    @NotBlank(message = "La marque est obligatoire")
    private String marque;

    @NotBlank(message = "Le modèle est obligatoire")
    private String modele;

    @NotBlank(message = "L'immatriculation est obligatoire")
    private String immatriculation;

    @NotBlank(message = "La couleur est obligatoire")
    private String couleur;

    @NotNull(message = "La capacité est obligatoire")
    @Positive(message = "La capacité doit être positive")
    private Integer capacite;

    @NotNull(message = "Le conducteur est obligatoire")
    private Long conducteurId;
}
