package ma.youcode.covoiturage.services.impl;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PassagerServiceImpl implements PassagerService {

    private final PassagerRepository passagerRepository;
    private final PassagerMapper passagerMapper;
    private final ReservationMapper reservationMapper;

    @Override
    public PassagerResponse create(PassagerRequest request) {
        log.info("Creation d'un passager en cours");
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
        List<PassagerResponse> passagers = passagerRepository.findAll().stream()
                .map(passagerMapper::toResponse)
                .toList();
        log.info("Liste des passagers recuperee, nombre={}", passagers.size());
        return passagers;
    }

    @Override
    public PassagerResponse findById(Long id) {
        Passager passager = passagerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Passager introuvable avec l'identifiant : " + id));

        log.info("Passager recupere, id={}", id);
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

        Passager updatedPassager = passagerRepository.save(passager);
        log.info("Passager mis a jour avec succes, id={}", id);
        return passagerMapper.toResponse(updatedPassager);
    }

    @Override
    public PassagerResponse searchByEmail(String email) {
        Passager passager = passagerRepository.findByEmail(email);
        if (passager == null) {
            throw new ResourceNotFoundException(
                    "Passager introuvable avec l'email : " + email);
        }

        log.info("Passager trouve par email, id={}", passager.getId());
        return passagerMapper.toResponse(passager);
    }

    @Override
    public List<ReservationResponse> findReservations(Long id) {
        Passager passager = passagerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Passager introuvable avec l'identifiant : " + id));

        List<ReservationResponse> reservations = passager.getReservations().stream()
                .map(reservationMapper::toResponse)
                .toList();
        log.info("Reservations du passager recuperees, passagerId={}, nombre={}", id, reservations.size());
        return reservations;
    }
}
