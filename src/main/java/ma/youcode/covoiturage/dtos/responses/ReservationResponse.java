package ma.youcode.covoiturage.dtos.responses;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.youcode.covoiturage.enums.StatutReservation;

@Getter
@Setter
@NoArgsConstructor
public class ReservationResponse {

    private Long id;
    private PointArretResponse pointMontee;
    private PointArretResponse pointDescente;
    private Integer nombrePlaces;
    private Double montantTotal;
    private LocalDateTime dateReservation;
    private StatutReservation statut;
    private Long passagerId;
    private Long trajetId;
}
