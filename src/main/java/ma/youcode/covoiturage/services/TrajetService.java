package ma.youcode.covoiturage.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    Page<TrajetResponse> search(
            String villeDepart,
            String villeArrivee,
            LocalDate date,
            Integer nombrePlaces,
            Double prixMax,
            Pageable pageable);

    TrajetResponse terminer(Long id);

    TrajetResponse annuler(Long id);
}
