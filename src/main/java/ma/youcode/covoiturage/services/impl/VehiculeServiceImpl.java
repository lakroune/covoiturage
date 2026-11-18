package ma.youcode.covoiturage.services.impl;

import java.util.List;

import ma.youcode.covoiturage.dtos.requests.VehiculeRequest;
import ma.youcode.covoiturage.dtos.responses.VehiculeResponse;
import ma.youcode.covoiturage.services.VehiculeService;
import org.springframework.stereotype.Service;

@Service
public class VehiculeServiceImpl implements VehiculeService {

    @Override
    public VehiculeResponse create(VehiculeRequest request) {
        return null;
    }

    @Override
    public VehiculeResponse findById(Long id) {
        return null;
    }

    @Override
    public List<VehiculeResponse> findByConducteurId(Long conducteurId) {
        return List.of();
    }

    @Override
    public VehiculeResponse update(Long id, VehiculeRequest request) {
        return null;
    }

    @Override
    public void delete(Long id) {
        // La suppression doit vérifier l'absence de trajet associé.
    }
}
