package ma.youcode.covoiturage.services;

import java.util.List;

import ma.youcode.covoiturage.dtos.requests.ReservationRequest;
import ma.youcode.covoiturage.dtos.responses.ReservationResponse;

public interface ReservationService {

    ReservationResponse create(ReservationRequest request);

    ReservationResponse findById(Long id);

    List<ReservationResponse> findByPassagerId(Long passagerId);

    ReservationResponse annuler(Long id);
}
