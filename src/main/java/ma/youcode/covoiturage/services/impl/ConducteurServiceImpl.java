package ma.youcode.covoiturage.services.impl;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ConducteurServiceImpl implements ConducteurService {

    private final ConducteurRepository conducteurRepository;
    private final ConducteurMapper conducteurMapper;
    private final VehiculeRepository vehiculeRepository;
    private final VehiculeMapper vehiculeMapper;
    private final TrajetRepository trajetRepository;
    private final TrajetMapper trajetMapper;

    @Override
    public ConducteurResponse create(ConducteurRequest request) {
        log.info("Création d'un conducteur en cours");

        if (conducteurRepository.existsByEmail(request.getEmail())) {
            log.warn("Création refusée : email déjà utilisé");
            throw new ConflictException("L'email doit être unique.");
        }

        if (conducteurRepository.existsByNumeroPermis(request.getNumeroPermis())) {
            log.warn("Création refusée : numéro de permis déjà utilisé");
            throw new ConflictException("Le numéro de permis doit être unique.");
        }

        Conducteur conducteur = conducteurMapper.toEntity(request);
        Conducteur savedConducteur = conducteurRepository.save(conducteur);
        log.info("Conducteur créé avec succès, id=", savedConducteur.getId());
        return conducteurMapper.toResponse(savedConducteur);
    }

    @Override
    public List<ConducteurResponse> findAll() {
        List<ConducteurResponse> conducteurs = conducteurRepository.findAll().stream()
                .map(conducteurMapper::toResponse)
                .toList();
        log.info("Liste des conducteurs recuperee, nombre={}", conducteurs.size());
        return conducteurs;
    }

    @Override
    public ConducteurResponse findById(Long id) {
        Conducteur conducteur = conducteurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conducteur introuvable avec l'identifiant : " + id));

        log.info("Conducteur recupere, id={}", id);
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

        Conducteur updatedConducteur = conducteurRepository.save(conducteur);
        log.info("Conducteur mis a jour avec succes, id={}", id);
        return conducteurMapper.toResponse(updatedConducteur);
    }

    @Override
    public void delete(Long id) {
        Conducteur conducteur = conducteurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conducteur introuvable avec l'identifiant : " + id));

        conducteurRepository.delete(conducteur);
        log.info("Conducteur supprime avec succes, id={}", id);
    }

    @Override
    public List<VehiculeResponse> findVehiculesByConducteur(Long id) {
        conducteurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conducteur introuvable avec l'identifiant : " + id));

        List<VehiculeResponse> vehicules = vehiculeRepository.findByConducteurId(id).stream()
                .map(vehiculeMapper::toResponse)
                .toList();
        log.info("Vehicules du conducteur recuperes, conducteurId={}, nombre={}", id, vehicules.size());
        return vehicules;
    }

    @Override
    public List<TrajetResponse> findTrajetsByConducteur(Long id) {
        conducteurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conducteur introuvable avec l'identifiant : " + id));

        List<TrajetResponse> trajets = trajetRepository.findByVehiculeConducteurId(id).stream()
                .map(trajetMapper::toResponse)
                .toList();
        log.info("Trajets du conducteur recuperes, conducteurId={}, nombre={}", id, trajets.size());
        return trajets;
    }

    @Override
    public ConducteurResponse searchByEmail(String email) {
        Conducteur conducteur = conducteurRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conducteur introuvable avec l'email : " + email));

        log.info("Conducteur trouve par email, id={}", conducteur.getId());
        return conducteurMapper.toResponse(conducteur);
    }

    @Override
    public List<ConducteurResponse> serachByNom(String nom) {
        List<ConducteurResponse> conducteurs = conducteurRepository.findByNomContainingIgnoreCase(nom).stream()
                .map(conducteurMapper::toResponse)
                .toList();
        log.info("Recherche de conducteurs par nom terminee, nombre={}", conducteurs.size());
        return conducteurs;
    }

}
