package ma.youcode.covoiturage.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.youcode.covoiturage.dtos.requests.ReservationRequest;
import ma.youcode.covoiturage.dtos.responses.ReservationResponse;
import ma.youcode.covoiturage.services.ReservationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
@Tag(name = "Reservation", description = "API pour gérer les réservations")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ReservationResponse create(@RequestBody ReservationRequest request) {
        return reservationService.create(request);
    }

    @GetMapping("/{id}")
    public ReservationResponse findById(@PathVariable Long id) {
        return reservationService.findById(id);
    }

    @GetMapping(params = "passagerId")
    public List<ReservationResponse> findByPassagerId(@RequestParam Long passagerId) {
        return reservationService.findByPassagerId(passagerId);
    }

    @PatchMapping("/{id}/annuler")
    public ReservationResponse annuler(@PathVariable Long id) {
        return reservationService.annuler(id);
    }
}
