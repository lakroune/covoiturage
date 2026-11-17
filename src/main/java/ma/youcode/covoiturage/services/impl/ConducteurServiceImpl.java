package ma.youcode.covoiturage.services.impl;

import java.util.List;

import ma.youcode.covoiturage.dtos.requests.ConducteurRequest;
import ma.youcode.covoiturage.dtos.responses.ConducteurResponse;
import ma.youcode.covoiturage.dtos.responses.TrajetResponse;
import ma.youcode.covoiturage.dtos.responses.VehiculeResponse;
import ma.youcode.covoiturage.services.ConducteurService;
import org.springframework.stereotype.Service;

@Service
public class ConducteurServiceImpl implements ConducteurService {

    @Override
    public ConducteurResponse create(ConducteurRequest request) {
        return null;
    }

    @Override
    public List<ConducteurResponse> findAll() {
        return null;
    }

    @Override
    public ConducteurResponse findById(Long id) {
        return null;
    }

    @Override
    public ConducteurResponse update(Long id, ConducteurRequest request) {
        return null;
    }

    @Override
    public void delete(Long id) {
    }

    @Override
    public List<VehiculeResponse> findVehiculesByConducteur(Long id) {
        return null;
    }

    @Override
    public List<TrajetResponse> findTrajetsByConducteur(Long id) {
        return null;
    }

}
