package ma.youcode.covoiturage.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import ma.youcode.covoiturage.dtos.requests.ConducteurRequest;
import ma.youcode.covoiturage.dtos.responses.ConducteurResponse;
import ma.youcode.covoiturage.dtos.responses.TrajetResponse;
import ma.youcode.covoiturage.dtos.responses.VehiculeResponse;
import ma.youcode.covoiturage.services.ConducteurService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/conducteurs")
@Tag(name = "Conducteur", description = "API pour gérer les conducteurs")

public class ConducteurController {
    private final ConducteurService conducteurService;

    @PostMapping
    public ConducteurResponse create(@RequestBody ConducteurRequest request) {
        return conducteurService.create(request);
    }

    @GetMapping
    public List<ConducteurResponse> findAll() {
        return conducteurService.findAll();
    }

    @GetMapping("/{id}")
    public ConducteurResponse findById(@PathVariable Long id) {
        return conducteurService.findById(id);
    }

    @PutMapping("/{id}")
    public ConducteurResponse update(
            @PathVariable Long id, @RequestBody ConducteurRequest request) {
        return conducteurService.update(id, request);
    }

    @GetMapping(value = "/search", params = "nom")
    public List<ConducteurResponse> searchByNom(@RequestParam String nom) {
        return conducteurService.serachByNom(nom);
    }

    @GetMapping(value = "/search", params = "email")
    public List<ConducteurResponse> searchByEmail(@RequestParam String email) {
        return conducteurService.searchByEmail(email);
    }

    @GetMapping("/{id}/vehicules")
    public List<VehiculeResponse> findVehiculesByConducteur(@PathVariable Long id) {
        return conducteurService.findVehiculesByConducteur(id);
    }

    @GetMapping("/{id}/trajets")
    public List<TrajetResponse> findTrajetsByConducteur(@PathVariable Long id) {
        return conducteurService.findTrajetsByConducteur(id);
    }
}
