package ma.youcode.covoiturage.services;

import java.util.List;

import ma.youcode.covoiturage.dtos.requests.VehiculeRequest;
import ma.youcode.covoiturage.dtos.responses.VehiculeResponse;

public interface VehiculeService {

    VehiculeResponse create(VehiculeRequest request);

    VehiculeResponse findById(Long id);

    List<VehiculeResponse> findByConducteurId(Long conducteurId);

    VehiculeResponse update(Long id, VehiculeRequest request);

    void delete(Long id);
}
