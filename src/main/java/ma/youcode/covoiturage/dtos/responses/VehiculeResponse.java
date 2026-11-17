package ma.youcode.covoiturage.dtos.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VehiculeResponse {

    private Long id;
    private String marque;
    private String modele;
    private String immatriculation;
    private String couleur;
    private Integer capacite;
    private Long conducteurId;
}
