package ma.youcode.covoiturage.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.youcode.covoiturage.dtos.requests.VehiculeRequest;
import ma.youcode.covoiturage.dtos.responses.VehiculeResponse;
import ma.youcode.covoiturage.services.VehiculeService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vehicules")
@Tag(name = "Vehicule", description = "API pour gérer les véhicules")
public class VehiculeController {

    private final VehiculeService vehiculeService;

    @PostMapping
    public VehiculeResponse create(@RequestBody VehiculeRequest request) {
        return vehiculeService.create(request);
    }

    @GetMapping("/{id}")
    public VehiculeResponse findById(@PathVariable Long id) {
        return vehiculeService.findById(id);
    }

    @GetMapping(params = "conducteurId")
    public List<VehiculeResponse> findByConducteurId(@RequestParam Long conducteurId) {
        return vehiculeService.findByConducteurId(conducteurId);
    }

    @PutMapping("/{id}")
    public VehiculeResponse update(
            @PathVariable Long id, @RequestBody VehiculeRequest request) {
        return vehiculeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        vehiculeService.delete(id);
    }
}
