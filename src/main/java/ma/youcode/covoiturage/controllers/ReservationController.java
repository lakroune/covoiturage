package ma.youcode.covoiturage.controllers;

import java.util.List;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.youcode.covoiturage.dtos.requests.ReservationRequest;
import ma.youcode.covoiturage.dtos.responses.ReservationResponse;
import ma.youcode.covoiturage.services.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
@Tag(name = "Reservation", description = "API pour gérer les réservations")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @Operation(
            summary = "Créer une réservation",
            description = "Crée une réservation pour un passager sur un segment de trajet.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réservation créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides ou places indisponibles"),
            @ApiResponse(responseCode = "404", description = "Passager, trajet ou point d'arrêt introuvable"),
            @ApiResponse(responseCode = "409", description = "Réservation impossible"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @ResponseStatus(HttpStatus.OK)
    public ReservationResponse create(@Valid @RequestBody ReservationRequest request) {
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
