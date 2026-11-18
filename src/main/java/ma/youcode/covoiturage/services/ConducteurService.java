package ma.youcode.covoiturage.services;

import java.util.List;

import ma.youcode.covoiturage.dtos.requests.ConducteurRequest;
import ma.youcode.covoiturage.dtos.requests.ConducteurRequestUpdate;
import ma.youcode.covoiturage.dtos.responses.ConducteurResponse;
import ma.youcode.covoiturage.dtos.responses.TrajetResponse;
import ma.youcode.covoiturage.dtos.responses.VehiculeResponse;

public interface ConducteurService {

    ConducteurResponse create(ConducteurRequest request);

    List<ConducteurResponse> findAll();

    ConducteurResponse findById(Long id);

    ConducteurResponse update(Long id, ConducteurRequestUpdate request);

    void delete(Long id);

    List<VehiculeResponse> findVehiculesByConducteur(Long id);

    List<TrajetResponse> findTrajetsByConducteur(Long id);

    List<ConducteurResponse> searchByEmail(String email);

    List<ConducteurResponse> serachByNom(String nom);
}
