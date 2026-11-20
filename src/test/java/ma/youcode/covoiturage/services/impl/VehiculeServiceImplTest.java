package ma.youcode.covoiturage.services.impl;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ma.youcode.covoiturage.dtos.requests.VehiculeRequest;
import ma.youcode.covoiturage.dtos.responses.VehiculeResponse;
import ma.youcode.covoiturage.entities.Conducteur;
import ma.youcode.covoiturage.entities.Vehicule;
import ma.youcode.covoiturage.mappers.VehiculeMapper;
import ma.youcode.covoiturage.repositories.ConducteurRepository;
import ma.youcode.covoiturage.repositories.VehiculeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VehiculeServiceImplTest {

    @Mock
    private VehiculeRepository vehiculeRepository;

    @Mock
    private ConducteurRepository conducteurRepository;

    @Mock
    private VehiculeMapper vehiculeMapper;

    @InjectMocks
    private VehiculeServiceImpl vehiculeService;

    @Test
    void doitCreerUnVehiculeValide() {
        VehiculeRequest request = new VehiculeRequest();
        request.setMarque("Renault");
        request.setModele("Clio");
        request.setImmatriculation("A-123");
        request.setCouleur("Rouge");
        request.setCapacite(4);
        request.setConducteurId(1L);

        Conducteur conducteur = new Conducteur();
        Vehicule vehicule = new Vehicule();
        VehiculeResponse response = new VehiculeResponse();

        when(vehiculeRepository.existsByImmatriculation("A-123")).thenReturn(false);
        when(conducteurRepository.findById(1L)).thenReturn(java.util.Optional.of(conducteur));
        when(vehiculeMapper.toEntity(request)).thenReturn(vehicule);
        when(vehiculeRepository.save(vehicule)).thenReturn(vehicule);
        when(vehiculeMapper.toResponse(vehicule)).thenReturn(response);

        VehiculeResponse result = vehiculeService.create(request);

        assertSame(response, result);
        assertSame(conducteur, vehicule.getConducteur());
        verify(vehiculeRepository).save(vehicule);
        verify(vehiculeMapper).toResponse(vehicule);
    }

    @Test
    void doitRetournerUnVehiculeParSonIdentifiant() {
        Vehicule vehicule = new Vehicule();
        VehiculeResponse response = new VehiculeResponse();

        when(vehiculeRepository.findById(1L)).thenReturn(java.util.Optional.of(vehicule));
        when(vehiculeMapper.toResponse(vehicule)).thenReturn(response);

        VehiculeResponse result = vehiculeService.findById(1L);

        assertSame(response, result);
        verify(vehiculeRepository).findById(1L);
        verify(vehiculeMapper).toResponse(vehicule);
    }
}
