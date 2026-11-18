package ma.youcode.covoiturage.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import ma.youcode.covoiturage.dtos.requests.TrajetRequest;
import ma.youcode.covoiturage.dtos.responses.PointArretResponse;
import ma.youcode.covoiturage.dtos.responses.ReservationResponse;
import ma.youcode.covoiturage.dtos.responses.TrajetResponse;
import ma.youcode.covoiturage.services.TrajetService;
import org.springframework.stereotype.Service;

@Service
public class TrajetServiceImpl implements TrajetService {

    @Override
    public TrajetResponse create(TrajetRequest request) {
        return null;
    }

    @Override
    public TrajetResponse findById(Long id) {
        return null;
    }

    @Override
    public List<TrajetResponse> findByConducteurId(Long conducteurId) {
        return List.of();
    }

    @Override
    public List<PointArretResponse> findPointsArret(Long id) {
        return List.of();
    }

    @Override
    public List<ReservationResponse> findReservations(Long id) {
        return List.of();
    }

    @Override
    public List<TrajetResponse> search(
            String villeDepart,
            String villeArrivee,
            LocalDate date,
            Integer nombrePlaces,
            BigDecimal prixMax,
            int page,
            int size) {
        return List.of();
    }

    @Override
    public TrajetResponse terminer(Long id) {
        return null;
    }

    @Override
    public TrajetResponse annuler(Long id) {
        return null;
    }
}
