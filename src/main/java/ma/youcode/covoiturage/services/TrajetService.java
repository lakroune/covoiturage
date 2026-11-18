package ma.youcode.covoiturage.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import ma.youcode.covoiturage.dtos.requests.TrajetRequest;
import ma.youcode.covoiturage.dtos.responses.PointArretResponse;
import ma.youcode.covoiturage.dtos.responses.ReservationResponse;
import ma.youcode.covoiturage.dtos.responses.TrajetResponse;

public interface TrajetService {

    TrajetResponse create(TrajetRequest request);

    TrajetResponse findById(Long id);

    List<TrajetResponse> findByConducteurId(Long conducteurId);

    List<PointArretResponse> findPointsArret(Long id);

    List<ReservationResponse> findReservations(Long id);

    List<TrajetResponse> search(
            String villeDepart,
            String villeArrivee,
            LocalDate date,
            Integer nombrePlaces,
            BigDecimal prixMax,
            int page,
            int size);

    TrajetResponse terminer(Long id);

    TrajetResponse annuler(Long id);
}
