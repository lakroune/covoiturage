package ma.youcode.covoiturage.services;

import java.util.List;

import ma.youcode.covoiturage.dtos.requests.PassagerRequest;
import ma.youcode.covoiturage.dtos.requests.PassagerRequestUpdate;
import ma.youcode.covoiturage.dtos.responses.PassagerResponse;
import ma.youcode.covoiturage.dtos.responses.ReservationResponse;

public interface PassagerService {

    PassagerResponse create(PassagerRequest request);

    List<PassagerResponse> findAll();

    PassagerResponse findById(Long id);

    PassagerResponse update(Long id, PassagerRequestUpdate request);

    PassagerResponse searchByEmail(String email);

    List<ReservationResponse> findReservations(Long id);
}
