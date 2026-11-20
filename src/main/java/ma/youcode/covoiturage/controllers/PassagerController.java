package ma.youcode.covoiturage.controllers;

import java.util.List;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.youcode.covoiturage.dtos.requests.PassagerRequest;
import ma.youcode.covoiturage.dtos.requests.PassagerRequestUpdate;
import ma.youcode.covoiturage.dtos.responses.PassagerResponse;
import ma.youcode.covoiturage.dtos.responses.ReservationResponse;
import ma.youcode.covoiturage.services.PassagerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/passagers")
@Tag(name = "Passager", description = "API pour gérer les passagers")
public class PassagerController {

    private final PassagerService passagerService;

    @PostMapping
    @Operation(
            summary = "Créer un passager",
            description = "Crée un nouveau passager avec ses informations personnelles.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Passager créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "409", description = "L'email existe déjà"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @ResponseStatus(HttpStatus.OK)
    public PassagerResponse create(@Valid @RequestBody PassagerRequest request) {
        return passagerService.create(request);
    }

    @GetMapping
    @Operation(
            summary = "Lister tous les passagers",
            description = "Retourne la liste de tous les passagers enregistrés.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des passagers récupérée avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @ResponseStatus(HttpStatus.OK)
    public List<PassagerResponse> findAll() {
        return passagerService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Consulter un passager",
            description = "Retourne les informations d'un passager à partir de son identifiant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Passager récupéré avec succès"),
            @ApiResponse(responseCode = "404", description = "Passager introuvable"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @ResponseStatus(HttpStatus.OK)
    public PassagerResponse findById(@PathVariable Long id) {
        return passagerService.findById(id);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Modifier le profil d'un passager",
            description = "Met à jour les informations modifiables d'un passager.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Passager modifié avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Passager introuvable"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @ResponseStatus(HttpStatus.OK)
    public PassagerResponse update(
            @PathVariable Long id, @Valid @RequestBody PassagerRequestUpdate request) {
        return passagerService.update(id, request);
    }

    @GetMapping(value = "/search", params = "email")
    public PassagerResponse searchByEmail(@RequestParam String email) {
        return passagerService.searchByEmail(email);
    }

    @GetMapping("/{id}/reservations")
    public List<ReservationResponse> findReservations(@PathVariable Long id) {
        return passagerService.findReservations(id);
    }
}
