package ma.youcode.covoiturage.services.impl;

import java.util.List;

import lombok.RequiredArgsConstructor;
import ma.youcode.covoiturage.dtos.requests.ConducteurRequest;
import ma.youcode.covoiturage.dtos.requests.ConducteurRequestUpdate;
import ma.youcode.covoiturage.dtos.responses.ConducteurResponse;
import ma.youcode.covoiturage.dtos.responses.TrajetResponse;
import ma.youcode.covoiturage.dtos.responses.VehiculeResponse;
import ma.youcode.covoiturage.entities.Conducteur;
import ma.youcode.covoiturage.exceptions.ConflictException;
import ma.youcode.covoiturage.exceptions.ResourceNotFoundException;
import ma.youcode.covoiturage.mappers.ConducteurMapper;
import ma.youcode.covoiturage.mappers.VehiculeMapper;
import ma.youcode.covoiturage.mappers.TrajetMapper;
import ma.youcode.covoiturage.repositories.ConducteurRepository;
import ma.youcode.covoiturage.repositories.TrajetRepository;
import ma.youcode.covoiturage.repositories.VehiculeRepository;
import ma.youcode.covoiturage.services.ConducteurService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConducteurServiceImpl implements ConducteurService {

    private final ConducteurRepository conducteurRepository;
    private final ConducteurMapper conducteurMapper;
    private final VehiculeRepository vehiculeRepository;
    private final VehiculeMapper vehiculeMapper;
    private final TrajetRepository trajetRepository;
    private final TrajetMapper trajetMapper;

    @Override
    public ConducteurResponse create(ConducteurRequest request) {
        if (conducteurRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("L'email doit être unique.");
        }

        if (conducteurRepository.existsByNumeroPermis(request.getNumeroPermis())) {
            throw new ConflictException("Le numéro de permis doit être unique.");
        }

        Conducteur conducteur = conducteurMapper.toEntity(request);
        Conducteur savedConducteur = conducteurRepository.save(conducteur);
        return conducteurMapper.toResponse(savedConducteur);
    }

    @Override
    public List<ConducteurResponse> findAll() {
        return conducteurRepository.findAll().stream()
                .map(conducteurMapper::toResponse)
                .toList();
    }

    @Override
    public ConducteurResponse findById(Long id) {
        Conducteur conducteur = conducteurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conducteur introuvable avec l'identifiant : " + id));

        return conducteurMapper.toResponse(conducteur);
    }

    @Override
    public ConducteurResponse update(Long id, ConducteurRequestUpdate request) {
        Conducteur conducteur = conducteurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conducteur introuvable avec l'identifiant : " + id));

        conducteur.setNom(request.getNom());
        conducteur.setPrenom(request.getPrenom());
        conducteur.setTelephone(request.getTelephone());

        return conducteurMapper.toResponse(conducteurRepository.save(conducteur));
    }

    @Override
    public void delete(Long id) {
        Conducteur conducteur = conducteurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conducteur introuvable avec l'identifiant : " + id));

        conducteurRepository.delete(conducteur);
    }

    @Override
    public List<VehiculeResponse> findVehiculesByConducteur(Long id) {
        conducteurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conducteur introuvable avec l'identifiant : " + id));

        return vehiculeRepository.findByConducteurId(id).stream()
                .map(vehiculeMapper::toResponse)
                .toList();
    }

    @Override
    public List<TrajetResponse> findTrajetsByConducteur(Long id) {
        conducteurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conducteur introuvable avec l'identifiant : " + id));

        return trajetRepository.findByConducteurId(id).stream()
                .map(trajetMapper::toResponse)
                .toList();
    }

    @Override
    public List<ConducteurResponse> searchByEmail(String email) {
        return List.of();
    }

    @Override
    public List<ConducteurResponse> serachByNom(String nom) {
        return List.of();
    }

}
