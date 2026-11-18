package ma.youcode.covoiturage.services.impl;

import java.util.List;

import lombok.RequiredArgsConstructor;
import ma.youcode.covoiturage.dtos.requests.PassagerRequest;
import ma.youcode.covoiturage.dtos.requests.PassagerRequestUpdate;
import ma.youcode.covoiturage.dtos.responses.PassagerResponse;
import ma.youcode.covoiturage.dtos.responses.ReservationResponse;
import ma.youcode.covoiturage.entities.Passager;
import ma.youcode.covoiturage.exceptions.ConflictException;
import ma.youcode.covoiturage.exceptions.ResourceNotFoundException;
import ma.youcode.covoiturage.mappers.PassagerMapper;
import ma.youcode.covoiturage.mappers.ReservationMapper;
import ma.youcode.covoiturage.repositories.PassagerRepository;
import ma.youcode.covoiturage.services.PassagerService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PassagerServiceImpl implements PassagerService {

    private final PassagerRepository passagerRepository;
    private final PassagerMapper passagerMapper;
    private final ReservationMapper reservationMapper;

    @Override
    public PassagerResponse create(PassagerRequest request) {
        if (passagerRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("L'email doit être unique.");
        }

        if (passagerRepository.existsByTelephone(request.getTelephone())) {
            throw new ConflictException("Le téléphone doit être unique.");
        }

        Passager passager = passagerMapper.toEntity(request);
        Passager savedPassager = passagerRepository.save(passager);
        return passagerMapper.toResponse(savedPassager);
    }

    @Override
    public List<PassagerResponse> findAll() {
        return passagerRepository.findAll().stream()
                .map(passagerMapper::toResponse)
                .toList();
    }

    @Override
    public PassagerResponse findById(Long id) {
        Passager passager = passagerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Passager introuvable avec l'identifiant : " + id));

        return passagerMapper.toResponse(passager);
    }

    @Override
    public PassagerResponse update(Long id, PassagerRequestUpdate request) {
        Passager passager = passagerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Passager introuvable avec l'identifiant : " + id));

        if (passagerRepository.existsByTelephoneAndIdNot(request.getTelephone(), id)) {
            throw new ConflictException("Le téléphone doit être unique.");
        }

        passager.setNom(request.getNom());
        passager.setPrenom(request.getPrenom());
        passager.setTelephone(request.getTelephone());

        return passagerMapper.toResponse(passagerRepository.save(passager));
    }

    @Override
    public List<PassagerResponse> searchByEmail(String email) {
        return passagerRepository.findByEmailContainingIgnoreCase(email).stream()
                .map(passagerMapper::toResponse)
                .toList();
    }

    @Override
    public List<ReservationResponse> findReservations(Long id) {
        Passager passager = passagerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Passager introuvable avec l'identifiant : " + id));

        return passager.getReservations().stream()
                .map(reservationMapper::toResponse)
                .toList();
    }
}
