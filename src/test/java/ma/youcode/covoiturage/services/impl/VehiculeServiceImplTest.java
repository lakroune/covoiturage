package ma.youcode.covoiturage.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import ma.youcode.covoiturage.dtos.requests.VehiculeRequest;
import ma.youcode.covoiturage.dtos.requests.VehiculeRequestUpdate;
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

    @Test
    void doitRetournerLesVehiculesDUnConducteur() {
        Conducteur conducteur = new Conducteur();
        Vehicule vehicule = new Vehicule();
        VehiculeResponse response = new VehiculeResponse();

        when(conducteurRepository.findById(1L)).thenReturn(java.util.Optional.of(conducteur));
        when(vehiculeRepository.findByConducteurId(1L)).thenReturn(List.of(vehicule));
        when(vehiculeMapper.toResponse(vehicule)).thenReturn(response);

        List<VehiculeResponse> result = vehiculeService.findByConducteurId(1L);

        assertSame(response, result.get(0));
        verify(conducteurRepository).findById(1L);
        verify(vehiculeRepository).findByConducteurId(1L);
        verify(vehiculeMapper).toResponse(vehicule);
    }

    @Test
    void doitMettreAJourUnVehicule() {
        VehiculeRequestUpdate request = new VehiculeRequestUpdate();
        request.setMarque("Peugeot");
        request.setModele("208");
        request.setImmatriculation("B-456");
        request.setCouleur("Bleu");
        request.setCapacite(5);

        Vehicule vehicule = new Vehicule();
        VehiculeResponse response = new VehiculeResponse();

        when(vehiculeRepository.findById(1L)).thenReturn(java.util.Optional.of(vehicule));
        when(vehiculeRepository.existsByImmatriculationAndIdNot("B-456", 1L)).thenReturn(false);
        when(vehiculeRepository.save(vehicule)).thenReturn(vehicule);
        when(vehiculeMapper.toResponse(vehicule)).thenReturn(response);

        VehiculeResponse result = vehiculeService.update(1L, request);

        assertSame(response, result);
        assertEquals("Peugeot", vehicule.getMarque());
        assertEquals("208", vehicule.getModele());
        assertEquals("B-456", vehicule.getImmatriculation());
        assertEquals("Bleu", vehicule.getCouleur());
        assertEquals(5, vehicule.getCapacite());
        verify(vehiculeRepository).save(vehicule);
        verify(vehiculeMapper).toResponse(vehicule);
    }

    @Test
    void doitSupprimerUnVehiculeSansTrajetAssocie() {
        Vehicule vehicule = new Vehicule();

        when(vehiculeRepository.findById(1L)).thenReturn(java.util.Optional.of(vehicule));

        vehiculeService.delete(1L);

        verify(vehiculeRepository).findById(1L);
        verify(vehiculeRepository).delete(vehicule);
    }
}
