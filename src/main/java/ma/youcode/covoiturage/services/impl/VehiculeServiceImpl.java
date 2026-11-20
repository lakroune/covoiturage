package ma.youcode.covoiturage.services.impl;

import java.util.List;

import ma.youcode.covoiturage.dtos.requests.VehiculeRequest;
import ma.youcode.covoiturage.dtos.requests.VehiculeRequestUpdate;
import ma.youcode.covoiturage.dtos.responses.VehiculeResponse;
import ma.youcode.covoiturage.entities.Conducteur;
import ma.youcode.covoiturage.entities.Vehicule;
import ma.youcode.covoiturage.exceptions.ConflictException;
import ma.youcode.covoiturage.exceptions.ResourceNotFoundException;
import ma.youcode.covoiturage.mappers.VehiculeMapper;
import ma.youcode.covoiturage.repositories.ConducteurRepository;
import ma.youcode.covoiturage.repositories.VehiculeRepository;
import ma.youcode.covoiturage.services.VehiculeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehiculeServiceImpl implements VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final ConducteurRepository conducteurRepository;
    private final VehiculeMapper vehiculeMapper;

    @Override
    public VehiculeResponse create(VehiculeRequest request) {
        log.info("Creation d'un vehicule en cours, conducteurId={}", request.getConducteurId());
        if (vehiculeRepository.existsByImmatriculation(request.getImmatriculation())) {
            throw new ConflictException("L'immatriculation doit être unique.");
        }

        Conducteur conducteur = conducteurRepository.findById(request.getConducteurId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conducteur introuvable avec l'identifiant : " + request.getConducteurId()));

        Vehicule vehicule = vehiculeMapper.toEntity(request);
        vehicule.setConducteur(conducteur);

        Vehicule savedVehicule = vehiculeRepository.save(vehicule);
        log.info("Vehicule cree avec succes, id={}", savedVehicule.getId());
        return vehiculeMapper.toResponse(savedVehicule);
    }

    @Override
    public VehiculeResponse findById(Long id) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Véhicule introuvable avec l'identifiant : " + id));

        log.info("Vehicule recupere, id={}", id);
        return vehiculeMapper.toResponse(vehicule);
    }

    @Override
    public List<VehiculeResponse> findByConducteurId(Long conducteurId) {
        conducteurRepository.findById(conducteurId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conducteur introuvable avec l'identifiant : " + conducteurId));

        List<VehiculeResponse> vehicules = vehiculeRepository.findByConducteurId(conducteurId).stream()
                .map(vehiculeMapper::toResponse)
                .toList();
        log.info("Vehicules du conducteur recuperes, conducteurId={}, nombre={}",
                conducteurId, vehicules.size());
        return vehicules;
    }

    @Override
    public VehiculeResponse update(Long id, VehiculeRequestUpdate request) {
        log.info("Mise a jour d'un vehicule en cours, id={}", id);
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Véhicule introuvable avec l'identifiant : " + id));

        if (vehiculeRepository.existsByImmatriculationAndIdNot(
                request.getImmatriculation(), id)) {
            throw new ConflictException("L'immatriculation doit être unique.");
        }

        vehicule.setMarque(request.getMarque());
        vehicule.setModele(request.getModele());
        vehicule.setImmatriculation(request.getImmatriculation());
        vehicule.setCouleur(request.getCouleur());
        vehicule.setCapacite(request.getCapacite());

        Vehicule updatedVehicule = vehiculeRepository.save(vehicule);
        log.info("Vehicule mis a jour avec succes, id={}", id);
        return vehiculeMapper.toResponse(updatedVehicule);
    }

    @Override
    public void delete(Long id) {
        log.info("Suppression d'un vehicule en cours, id={}", id);
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Véhicule introuvable avec l'identifiant : " + id));

        if (!vehicule.getTrajets().isEmpty()) {
            throw new ConflictException(
                    "Impossible de supprimer le véhicule car il est associé à un trajet.");
        }

        vehiculeRepository.delete(vehicule);
        log.info("Vehicule supprime avec succes, id={}", id);
    }
}
