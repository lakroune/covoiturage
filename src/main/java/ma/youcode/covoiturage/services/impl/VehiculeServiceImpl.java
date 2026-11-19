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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final ConducteurRepository conducteurRepository;
    private final VehiculeMapper vehiculeMapper;

    @Override
    public VehiculeResponse create(VehiculeRequest request) {
        if (vehiculeRepository.existsByImmatriculation(request.getImmatriculation())) {
            throw new ConflictException("L'immatriculation doit être unique.");
        }

        Conducteur conducteur = conducteurRepository.findById(request.getConducteurId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conducteur introuvable avec l'identifiant : " + request.getConducteurId()));

        Vehicule vehicule = vehiculeMapper.toEntity(request);
        vehicule.setConducteur(conducteur);

        return vehiculeMapper.toResponse(vehiculeRepository.save(vehicule));
    }

    @Override
    public VehiculeResponse findById(Long id) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Véhicule introuvable avec l'identifiant : " + id));

        return vehiculeMapper.toResponse(vehicule);
    }

    @Override
    public List<VehiculeResponse> findByConducteurId(Long conducteurId) {
        conducteurRepository.findById(conducteurId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conducteur introuvable avec l'identifiant : " + conducteurId));

        return vehiculeRepository.findByConducteurId(conducteurId).stream()
                .map(vehiculeMapper::toResponse)
                .toList();
    }

    @Override
    public VehiculeResponse update(Long id, VehiculeRequestUpdate request) {
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

        return vehiculeMapper.toResponse(vehiculeRepository.save(vehicule));
    }

    @Override
    public void delete(Long id) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Véhicule introuvable avec l'identifiant : " + id));

        if (!vehicule.getTrajets().isEmpty()) {
            throw new ConflictException(
                    "Impossible de supprimer le véhicule car il est associé à un trajet.");
        }

        vehiculeRepository.delete(vehicule);
    }
}
