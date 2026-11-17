package ma.youcode.covoiturage.dtos.responses;

import java.math.BigDecimal;
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
    private BigDecimal montantTotal;
    private LocalDateTime dateReservation;
    private StatutReservation statut;
    private Long passagerId;
    private Long trajetId;
}
