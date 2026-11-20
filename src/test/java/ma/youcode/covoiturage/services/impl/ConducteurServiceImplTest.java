package ma.youcode.covoiturage.services.impl;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import ma.youcode.covoiturage.dtos.requests.ConducteurRequest;
import ma.youcode.covoiturage.dtos.requests.ConducteurRequestUpdate;
import ma.youcode.covoiturage.dtos.responses.ConducteurResponse;
import ma.youcode.covoiturage.dtos.responses.TrajetResponse;
import ma.youcode.covoiturage.dtos.responses.VehiculeResponse;
import ma.youcode.covoiturage.entities.Conducteur;
import ma.youcode.covoiturage.entities.Trajet;
import ma.youcode.covoiturage.entities.Vehicule;
import ma.youcode.covoiturage.mappers.ConducteurMapper;
import ma.youcode.covoiturage.mappers.TrajetMapper;
import ma.youcode.covoiturage.mappers.VehiculeMapper;
import ma.youcode.covoiturage.repositories.ConducteurRepository;
import ma.youcode.covoiturage.repositories.TrajetRepository;
import ma.youcode.covoiturage.repositories.VehiculeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConducteurServiceImplTest {

    @Mock
    private ConducteurRepository conducteurRepository;

    @Mock
    private ConducteurMapper conducteurMapper;

    @Mock
    private VehiculeRepository vehiculeRepository;

    @Mock
    private VehiculeMapper vehiculeMapper;

    @Mock
    private TrajetRepository trajetRepository;

    @Mock
    private TrajetMapper trajetMapper;

    @InjectMocks
    private ConducteurServiceImpl conducteurService;

    @Test
    void doitCreerUnConducteurValide() {
        ConducteurRequest request = new ConducteurRequest();
        request.setNom("Alami");
        request.setPrenom("Youssef");
        request.setEmail("youssef.alami@example.com");
        request.setTelephone("0612345678");
        request.setNumeroPermis("P123456");

        Conducteur conducteur = new Conducteur();
        ConducteurResponse response = new ConducteurResponse();

        when(conducteurRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(conducteurRepository.existsByNumeroPermis(request.getNumeroPermis())).thenReturn(false);
        when(conducteurMapper.toEntity(request)).thenReturn(conducteur);
        when(conducteurRepository.save(conducteur)).thenReturn(conducteur);
        when(conducteurMapper.toResponse(conducteur)).thenReturn(response);

        ConducteurResponse result = conducteurService.create(request);

        assertSame(response, result);
        verify(conducteurRepository).existsByEmail(request.getEmail());
        verify(conducteurRepository).existsByNumeroPermis(request.getNumeroPermis());
        verify(conducteurRepository).save(any(Conducteur.class));
        verify(conducteurMapper).toResponse(conducteur);
    }

    @Test
    void doitRetournerTousLesConducteurs() {
        Conducteur premier = new Conducteur();
        Conducteur second = new Conducteur();
        ConducteurResponse premiereResponse = new ConducteurResponse();
        ConducteurResponse secondeResponse = new ConducteurResponse();

        when(conducteurRepository.findAll()).thenReturn(List.of(premier, second));
        when(conducteurMapper.toResponse(premier)).thenReturn(premiereResponse);
        when(conducteurMapper.toResponse(second)).thenReturn(secondeResponse);

        List<ConducteurResponse> result = conducteurService.findAll();

        assertEquals(List.of(premiereResponse, secondeResponse), result);
        verify(conducteurRepository).findAll();
    }

    @Test
    void doitRetournerUnConducteurParSonIdentifiant() {
        Long id = 1L;
        Conducteur conducteur = new Conducteur();
        ConducteurResponse response = new ConducteurResponse();

        when(conducteurRepository.findById(id)).thenReturn(java.util.Optional.of(conducteur));
        when(conducteurMapper.toResponse(conducteur)).thenReturn(response);

        ConducteurResponse result = conducteurService.findById(id);

        assertSame(response, result);
        verify(conducteurRepository).findById(id);
        verify(conducteurMapper).toResponse(conducteur);
    }

    @Test
    void doitMettreAJourUnConducteur() {
        Long id = 1L;
        Conducteur conducteur = new Conducteur();
        ConducteurRequestUpdate request = new ConducteurRequestUpdate();
        request.setNom("Alami");
        request.setPrenom("Youssef");
        request.setTelephone("0612345678");
        ConducteurResponse response = new ConducteurResponse();

        when(conducteurRepository.findById(id)).thenReturn(java.util.Optional.of(conducteur));
        when(conducteurRepository.save(conducteur)).thenReturn(conducteur);
        when(conducteurMapper.toResponse(conducteur)).thenReturn(response);

        ConducteurResponse result = conducteurService.update(id, request);

        assertSame(response, result);
        verify(conducteurRepository).save(conducteur);
        verify(conducteurMapper).toResponse(conducteur);
    }

    @Test
    void doitSupprimerUnConducteur() {
        Long id = 1L;
        Conducteur conducteur = new Conducteur();

        when(conducteurRepository.findById(id)).thenReturn(java.util.Optional.of(conducteur));

        conducteurService.delete(id);

        verify(conducteurRepository).findById(id);
        verify(conducteurRepository).delete(conducteur);
    }

    @Test
    void doitRetournerLesVehiculesDUnConducteur() {
        Long id = 1L;
        Conducteur conducteur = new Conducteur();
        Vehicule vehicule = new Vehicule();
        VehiculeResponse response = new VehiculeResponse();

        when(conducteurRepository.findById(id)).thenReturn(java.util.Optional.of(conducteur));
        when(vehiculeRepository.findByConducteurId(id)).thenReturn(List.of(vehicule));
        when(vehiculeMapper.toResponse(vehicule)).thenReturn(response);

        List<VehiculeResponse> result = conducteurService.findVehiculesByConducteur(id);

        assertEquals(List.of(response), result);
        verify(vehiculeRepository).findByConducteurId(id);
    }

    @Test
    void doitRetournerLesTrajetsDUnConducteur() {
        Long id = 1L;
        Conducteur conducteur = new Conducteur();
        Trajet trajet = new Trajet();
        TrajetResponse response = new TrajetResponse();

        when(conducteurRepository.findById(id)).thenReturn(java.util.Optional.of(conducteur));
        when(trajetRepository.findByVehiculeConducteurId(id)).thenReturn(List.of(trajet));
        when(trajetMapper.toResponse(trajet)).thenReturn(response);

        List<TrajetResponse> result = conducteurService.findTrajetsByConducteur(id);

        assertEquals(List.of(response), result);
        verify(trajetRepository).findByVehiculeConducteurId(id);
    }
}
