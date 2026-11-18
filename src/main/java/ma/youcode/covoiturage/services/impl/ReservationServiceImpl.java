package ma.youcode.covoiturage.services.impl;

import java.util.List;

import ma.youcode.covoiturage.dtos.requests.ReservationRequest;
import ma.youcode.covoiturage.dtos.responses.ReservationResponse;
import ma.youcode.covoiturage.services.ReservationService;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Override
    public ReservationResponse create(ReservationRequest request) {
        return null;
    }

    @Override
    public ReservationResponse findById(Long id) {
        return null;
    }

    @Override
    public List<ReservationResponse> findByPassagerId(Long passagerId) {
        return List.of();
    }

    @Override
    public ReservationResponse annuler(Long id) {
        return null;
    }
}
