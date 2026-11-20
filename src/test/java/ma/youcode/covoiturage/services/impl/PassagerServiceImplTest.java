package ma.youcode.covoiturage.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import ma.youcode.covoiturage.dtos.requests.PassagerRequest;
import ma.youcode.covoiturage.dtos.requests.PassagerRequestUpdate;
import ma.youcode.covoiturage.dtos.responses.PassagerResponse;
import ma.youcode.covoiturage.dtos.responses.ReservationResponse;
import ma.youcode.covoiturage.entities.Passager;
import ma.youcode.covoiturage.entities.Reservation;
import ma.youcode.covoiturage.mappers.PassagerMapper;
import ma.youcode.covoiturage.mappers.ReservationMapper;
import ma.youcode.covoiturage.repositories.PassagerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PassagerServiceImplTest {

    @Mock
    private PassagerRepository passagerRepository;

    @Mock
    private PassagerMapper passagerMapper;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private PassagerServiceImpl passagerService;

    @Test
    void doitCreerUnPassagerValide() {
        PassagerRequest request = new PassagerRequest();
        request.setNom("Alami");
        request.setPrenom("Sara");
        request.setEmail("sara.alami@example.com");
        request.setTelephone("0612345678");

        Passager passager = new Passager();
        PassagerResponse response = new PassagerResponse();

        when(passagerRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passagerRepository.existsByTelephone(request.getTelephone())).thenReturn(false);
        when(passagerMapper.toEntity(request)).thenReturn(passager);
        when(passagerRepository.save(passager)).thenReturn(passager);
        when(passagerMapper.toResponse(passager)).thenReturn(response);

        PassagerResponse result = passagerService.create(request);

        assertSame(response, result);
        verify(passagerRepository).save(passager);
        verify(passagerMapper).toResponse(passager);
    }

    @Test
    void doitRetournerTousLesPassagers() {
        Passager premier = new Passager();
        Passager second = new Passager();
        PassagerResponse premiereResponse = new PassagerResponse();
        PassagerResponse secondeResponse = new PassagerResponse();

        when(passagerRepository.findAll()).thenReturn(List.of(premier, second));
        when(passagerMapper.toResponse(premier)).thenReturn(premiereResponse);
        when(passagerMapper.toResponse(second)).thenReturn(secondeResponse);

        List<PassagerResponse> result = passagerService.findAll();

        assertEquals(List.of(premiereResponse, secondeResponse), result);
        verify(passagerRepository).findAll();
    }

    @Test
    void doitRetournerUnPassagerParSonIdentifiant() {
        Long id = 1L;
        Passager passager = new Passager();
        PassagerResponse response = new PassagerResponse();

        when(passagerRepository.findById(id)).thenReturn(java.util.Optional.of(passager));
        when(passagerMapper.toResponse(passager)).thenReturn(response);

        PassagerResponse result = passagerService.findById(id);

        assertSame(response, result);
        verify(passagerRepository).findById(id);
        verify(passagerMapper).toResponse(passager);
    }

    @Test
    void doitMettreAJourUnPassager() {
        Long id = 1L;
        Passager passager = new Passager();
        PassagerRequestUpdate request = new PassagerRequestUpdate();
        request.setNom("Alami");
        request.setPrenom("Sara");
        request.setTelephone("0612345678");
        PassagerResponse response = new PassagerResponse();

        when(passagerRepository.findById(id)).thenReturn(java.util.Optional.of(passager));
        when(passagerRepository.existsByTelephoneAndIdNot(request.getTelephone(), id)).thenReturn(false);
        when(passagerRepository.save(passager)).thenReturn(passager);
        when(passagerMapper.toResponse(passager)).thenReturn(response);

        PassagerResponse result = passagerService.update(id, request);

        assertSame(response, result);
        verify(passagerRepository).save(passager);
        verify(passagerMapper).toResponse(passager);
    }

    @Test
    void doitRechercherUnPassagerParEmail() {
        String email = "sara.alami@example.com";
        Passager passager = new Passager();
        PassagerResponse response = new PassagerResponse();

        when(passagerRepository.findByEmail(email)).thenReturn(passager);
        when(passagerMapper.toResponse(passager)).thenReturn(response);

        PassagerResponse result = passagerService.searchByEmail(email);

        assertSame(response, result);
        verify(passagerRepository).findByEmail(email);
    }
}
