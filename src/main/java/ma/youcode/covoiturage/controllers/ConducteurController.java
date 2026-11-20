package ma.youcode.covoiturage.controllers;

import java.util.List;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import ma.youcode.covoiturage.dtos.requests.ConducteurRequest;
import ma.youcode.covoiturage.dtos.requests.ConducteurRequestUpdate;
import ma.youcode.covoiturage.dtos.responses.ConducteurResponse;
import ma.youcode.covoiturage.dtos.responses.TrajetResponse;
import ma.youcode.covoiturage.dtos.responses.VehiculeResponse;
import ma.youcode.covoiturage.services.ConducteurService;
import org.springframework.http.HttpStatus;
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
@AllArgsConstructor
@RequestMapping("/api/v1/conducteurs")
@Tag(name = "Conducteur", description = "API pour gérer les conducteurs")

public class ConducteurController {
    private final ConducteurService conducteurService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer un conducteur", description = "Crée un nouveau conducteur avec ses informations personnelles et professionnelles.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Conducteur créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "409", description = "L'email ou le numéro de permis existe déjà")
    })
    public ConducteurResponse create(@Valid @RequestBody ConducteurRequest request) {
        return conducteurService.create(request);
    }



    

    @GetMapping
    @Operation(
            summary = "Lister tous les conducteurs",
            description = "Retourne la liste de tous les conducteurs enregistrés.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des conducteurs récupérée avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @ResponseStatus(HttpStatus.OK)
    public List<ConducteurResponse> findAll() {
        return conducteurService.findAll();
    }



    
    @GetMapping("/{id}")
    @Operation(
            summary = "Consulter un conducteur",
            description = "Retourne les informations d'un conducteur à partir de son identifiant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conducteur récupéré avec succès"),
            @ApiResponse(responseCode = "404", description = "Conducteur introuvable"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @ResponseStatus(HttpStatus.OK)
    public ConducteurResponse findById(@PathVariable Long id) {
        return conducteurService.findById(id);
    }



    @PutMapping("/{id}")
    @Operation(
            summary = "Modifier le profil d'un conducteur",
            description = "Met à jour les informations modifiables d'un conducteur à partir de son identifiant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conducteur modifié avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Conducteur introuvable"),
            @ApiResponse(responseCode = "409", description = "Donnée déjà utilisée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @ResponseStatus(HttpStatus.OK)
    public ConducteurResponse update(
            @PathVariable Long id, @Valid @RequestBody ConducteurRequestUpdate request) {
        return conducteurService.update(id, request);
    }

    @GetMapping(value = "/search", params = "nom")
    @Operation(
            summary = "Rechercher des conducteurs par nom",
            description = "Retourne les conducteurs dont le nom correspond au critère de recherche.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Résultats de recherche récupérés avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @ResponseStatus(HttpStatus.OK)
    public List<ConducteurResponse> searchByNom(@RequestParam String nom) {
        return conducteurService.serachByNom(nom);
    }

    @GetMapping(value = "/search", params = "email")
    @Operation(
            summary = "Rechercher un conducteur par email",
            description = "Retourne le conducteur associé à l'adresse email indiquée.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conducteur récupéré avec succès"),
            @ApiResponse(responseCode = "404", description = "Conducteur introuvable"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @ResponseStatus(HttpStatus.OK)
    public ConducteurResponse searchByEmail(@RequestParam String email) {
        return conducteurService.searchByEmail(email);
    }

    @GetMapping("/{id}/vehicules")
    @Operation(
            summary = "Consulter les véhicules d'un conducteur",
            description = "Retourne tous les véhicules associés au conducteur indiqué.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des véhicules récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Conducteur introuvable"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @ResponseStatus(HttpStatus.OK)
    public List<VehiculeResponse> findVehiculesByConducteur(@PathVariable Long id) {
        return conducteurService.findVehiculesByConducteur(id);
    }

    @GetMapping("/{id}/trajets")
    @Operation(
            summary = "Consulter les trajets d'un conducteur",
            description = "Retourne tous les trajets publiés ou enregistrés par le conducteur indiqué.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des trajets récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Conducteur introuvable"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @ResponseStatus(HttpStatus.OK)
    public List<TrajetResponse> findTrajetsByConducteur(@PathVariable Long id) {
        return conducteurService.findTrajetsByConducteur(id);
    }
}
