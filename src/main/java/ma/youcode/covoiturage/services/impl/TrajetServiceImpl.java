package ma.youcode.covoiturage.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;
import ma.youcode.covoiturage.dtos.requests.TrajetRequest;
import ma.youcode.covoiturage.dtos.responses.PointArretResponse;
import ma.youcode.covoiturage.dtos.responses.ReservationResponse;
import ma.youcode.covoiturage.dtos.responses.TrajetResponse;
import ma.youcode.covoiturage.entities.Trajet;
import ma.youcode.covoiturage.entities.Vehicule;
import ma.youcode.covoiturage.enums.StatutTrajet;
import ma.youcode.covoiturage.exceptions.ResourceNotFoundException;
import ma.youcode.covoiturage.mappers.TrajetMapper;
import ma.youcode.covoiturage.mappers.PointArretMapper;
import ma.youcode.covoiturage.mappers.ReservationMapper;
import ma.youcode.covoiturage.repositories.PointArretRepository;
import ma.youcode.covoiturage.repositories.TrajetRepository;
import ma.youcode.covoiturage.repositories.VehiculeRepository;
import ma.youcode.covoiturage.services.TrajetService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrajetServiceImpl implements TrajetService {

    private final TrajetRepository trajetRepository;
    private final PointArretRepository pointArretRepository;
    private final VehiculeRepository vehiculeRepository;
    private final TrajetMapper trajetMapper;
    private final PointArretMapper pointArretMapper;
    private final ReservationMapper reservationMapper;

    @Override
    public TrajetResponse create(TrajetRequest request) {
        Vehicule vehicule = vehiculeRepository.findById(request.getVehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vehicule introuvable avec l'identifiant : " + request.getVehiculeId()));

        Trajet trajet = trajetMapper.toEntity(request);
        trajet.setVehicule(vehicule);
        trajet.setStatut(StatutTrajet.PUBLIE);

        return trajetMapper.toResponse(trajetRepository.save(trajet));
    }

    @Override
    public TrajetResponse findById(Long id) {
        Trajet trajet = trajetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Trajet introuvable avec l'identifiant : " + id));

        return trajetMapper.toResponse(trajet);
    }

    @Override
    public List<TrajetResponse> findByConducteurId(Long conducteurId) {
        return trajetRepository.findByVehiculeConducteurId(conducteurId).stream()
                .map(trajetMapper::toResponse)
                .toList();
    }

    @Override
    public List<PointArretResponse> findPointsArret(Long id) {
        trajetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Trajet introuvable avec l'identifiant : " + id));

        return pointArretRepository.findByTrajetIdOrderByOrdreAsc(id).stream()
                .map(pointArretMapper::toResponse)
                .toList();
    }

    @Override
    public List<ReservationResponse> findReservations(Long id) {
        Trajet trajet = trajetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Trajet introuvable avec l'identifiant : " + id));

        return trajet.getReservations().stream()
                .map(reservationMapper::toResponse)
                .toList();
    }

    @Override
    public List<TrajetResponse> search(
            String villeDepart,
            String villeArrivee,
            LocalDate date,
            Integer nombrePlaces,
            BigDecimal prixMax,
            int page,
            int size) {
        return List.of();
    }

    @Override
    public TrajetResponse terminer(Long id) {
        Trajet trajet = trajetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Trajet introuvable avec l'identifiant : " + id));

        trajet.setStatut(StatutTrajet.TERMINE);
        return trajetMapper.toResponse(trajetRepository.save(trajet));
    }

    @Override
    public TrajetResponse annuler(Long id) {
        return null;
    }
}
