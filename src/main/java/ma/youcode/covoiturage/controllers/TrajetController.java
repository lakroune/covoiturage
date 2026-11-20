package ma.youcode.covoiturage.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import ma.youcode.covoiturage.dtos.requests.TrajetRequest;
import ma.youcode.covoiturage.dtos.responses.PointArretResponse;
import ma.youcode.covoiturage.dtos.responses.ReservationResponse;
import ma.youcode.covoiturage.dtos.responses.TrajetResponse;
import ma.youcode.covoiturage.services.TrajetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trajets")
@Tag (name = "Trajet", description = "API pour gérer les trajets")
public class TrajetController {

    private final TrajetService trajetService;

    @PostMapping
    public TrajetResponse create(@Valid @RequestBody TrajetRequest request) {
        return trajetService.create(request);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Consulter un trajet",
            description = "Retourne le détail d'un trajet à partir de son identifiant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Trajet récupéré avec succès"),
            @ApiResponse(responseCode = "404", description = "Trajet introuvable"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @ResponseStatus(HttpStatus.OK)
    public TrajetResponse findById(@PathVariable Long id) {
        return trajetService.findById(id);
    }

    @GetMapping(params = "conducteurId")
    public List<TrajetResponse> findByConducteurId(@RequestParam Long conducteurId) {
        return trajetService.findByConducteurId(conducteurId);
    }

    @GetMapping("/{id}/points-arret")
    public List<PointArretResponse> findPointsArret(@PathVariable Long id) {
        return trajetService.findPointsArret(id);
    }

    @GetMapping("/{id}/reservations")
    public List<ReservationResponse> findReservations(@PathVariable Long id) {
        return trajetService.findReservations(id);
    }

    @GetMapping("/recherche")
    public Page<TrajetResponse> search(
            @RequestParam String villeDepart,
            @RequestParam String villeArrivee,
            @RequestParam LocalDate date,
            @RequestParam Integer nombrePlaces,
            @RequestParam BigDecimal prixMax,
            @PageableDefault(size = 10) Pageable pageable) {
        return trajetService.search(
                villeDepart, villeArrivee, date, nombrePlaces, prixMax, pageable);
    }

    @PatchMapping("/{id}/terminer")
    public TrajetResponse terminer(@PathVariable Long id) {
        return trajetService.terminer(id);
    }

    @PatchMapping("/{id}/annuler")
    public TrajetResponse annuler(@PathVariable Long id) {
        return trajetService.annuler(id);
    }
}
