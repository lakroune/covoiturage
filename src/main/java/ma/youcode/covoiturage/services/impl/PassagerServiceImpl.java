package ma.youcode.covoiturage.services.impl;

import java.util.List;

import ma.youcode.covoiturage.dtos.requests.PassagerRequest;
import ma.youcode.covoiturage.dtos.responses.PassagerResponse;
import ma.youcode.covoiturage.dtos.responses.ReservationResponse;
import ma.youcode.covoiturage.services.PassagerService;
import org.springframework.stereotype.Service;

@Service
public class PassagerServiceImpl implements PassagerService {

    @Override
    public PassagerResponse create(PassagerRequest request) {
        return null;
    }

    @Override
    public List<PassagerResponse> findAll() {
        return null;
    }

    @Override
    public PassagerResponse findById(Long id) {
        return null;
    }

    @Override
    public PassagerResponse update(Long id, PassagerRequest request) {
        return null;
    }

    @Override
    public List<PassagerResponse> searchByEmail(String email) {
        return List.of();
    }

    @Override
    public List<ReservationResponse> findReservations(Long id) {
        return List.of();
    }
}
