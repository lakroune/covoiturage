package ma.youcode.covoiturage.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.youcode.covoiturage.dtos.requests.PassagerRequest;
import ma.youcode.covoiturage.dtos.responses.PassagerResponse;
import ma.youcode.covoiturage.dtos.responses.ReservationResponse;
import ma.youcode.covoiturage.services.PassagerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/passagers")
@Tag(name = "Passager", description = "API pour gérer les passagers")
public class PassagerController {

    private final PassagerService passagerService;

    @PostMapping
    public PassagerResponse create(@RequestBody PassagerRequest request) {
        return passagerService.create(request);
    }

    @GetMapping
    public List<PassagerResponse> findAll() {
        return passagerService.findAll();
    }

    @GetMapping("/{id}")
    public PassagerResponse findById(@PathVariable Long id) {
        return passagerService.findById(id);
    }

    @PutMapping("/{id}")
    public PassagerResponse update(@PathVariable Long id, @RequestBody PassagerRequest request) {
        return passagerService.update(id, request);
    }

    @GetMapping(value = "/search", params = "email")
    public List<PassagerResponse> searchByEmail(@RequestParam String email) {
        return passagerService.searchByEmail(email);
    }

    @GetMapping("/{id}/reservations")
    public List<ReservationResponse> findReservations(@PathVariable Long id) {
        return passagerService.findReservations(id);
    }
}
