package ma.youcode.covoiturage.controllers;

import java.util.List;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.youcode.covoiturage.dtos.requests.VehiculeRequest;
import ma.youcode.covoiturage.dtos.requests.VehiculeRequestUpdate;
import ma.youcode.covoiturage.dtos.responses.VehiculeResponse;
import ma.youcode.covoiturage.services.VehiculeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vehicules")
@Tag(name = "Vehicule", description = "API pour gérer les véhicules")
public class VehiculeController {

    private final VehiculeService vehiculeService;

    @PostMapping
    @Operation(
            summary = "Ajouter un véhicule",
            description = "Ajoute un véhicule associé à un conducteur.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Véhicule ajouté avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Conducteur introuvable"),
            @ApiResponse(responseCode = "409", description = "L'immatriculation existe déjà"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @ResponseStatus(HttpStatus.OK)
    public VehiculeResponse create(@Valid @RequestBody VehiculeRequest request) {
        return vehiculeService.create(request);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Consulter un véhicule",
            description = "Retourne les informations d'un véhicule à partir de son identifiant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Véhicule récupéré avec succès"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @ResponseStatus(HttpStatus.OK)
    public VehiculeResponse findById(@PathVariable Long id) {
        return vehiculeService.findById(id);
    }

    @GetMapping(params = "conducteurId")
    @Operation(
            summary = "Lister les véhicules d'un conducteur",
            description = "Retourne tous les véhicules associés au conducteur indiqué.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des véhicules récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Conducteur introuvable"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @ResponseStatus(HttpStatus.OK)
    public List<VehiculeResponse> findByConducteurId(@RequestParam Long conducteurId) {
        return vehiculeService.findByConducteurId(conducteurId);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Modifier un véhicule",
            description = "Met à jour les informations d'un véhicule existant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Véhicule modifié avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable"),
            @ApiResponse(responseCode = "409", description = "L'immatriculation existe déjà"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @ResponseStatus(HttpStatus.OK)
    public VehiculeResponse update(
            @PathVariable Long id, @Valid @RequestBody VehiculeRequestUpdate request) {
        return vehiculeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer un véhicule",
            description = "Supprime un véhicule lorsqu'il n'est associé à aucun trajet.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Véhicule supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable"),
            @ApiResponse(responseCode = "409", description = "Véhicule associé à un trajet"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        vehiculeService.delete(id);
    }
}
