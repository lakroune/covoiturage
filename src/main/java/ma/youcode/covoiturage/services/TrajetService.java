package ma.youcode.covoiturage.services;

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

    List<TrajetResponse> search();

    TrajetResponse terminer(Long id);

    TrajetResponse annuler(Long id);
}
