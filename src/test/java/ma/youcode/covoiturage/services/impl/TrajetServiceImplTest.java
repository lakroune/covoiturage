package ma.youcode.covoiturage.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import ma.youcode.covoiturage.dtos.requests.PointArretRequest;
import ma.youcode.covoiturage.dtos.requests.TrajetRequest;
import ma.youcode.covoiturage.dtos.responses.TrajetResponse;
import ma.youcode.covoiturage.entities.Trajet;
import ma.youcode.covoiturage.entities.Vehicule;
import ma.youcode.covoiturage.enums.StatutTrajet;
import ma.youcode.covoiturage.mappers.PointArretMapper;
import ma.youcode.covoiturage.mappers.ReservationMapper;
import ma.youcode.covoiturage.mappers.TrajetMapper;
import ma.youcode.covoiturage.repositories.PointArretRepository;
import ma.youcode.covoiturage.repositories.TrajetRepository;
import ma.youcode.covoiturage.repositories.VehiculeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrajetServiceImplTest {

    @Mock
    private TrajetRepository trajetRepository;

    @Mock
    private PointArretRepository pointArretRepository;

    @Mock
    private VehiculeRepository vehiculeRepository;

    @Mock
    private TrajetMapper trajetMapper;

    @Mock
    private PointArretMapper pointArretMapper;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private TrajetServiceImpl trajetService;

    @Test
    void doitCreerEtPublierUnTrajetValide() {
        LocalDateTime depart = LocalDateTime.now().plusDays(1);
        TrajetRequest request = new TrajetRequest();
        request.setVehiculeId(2L);
        request.setPlacesProposees(3);
        request.setPointsArret(List.of(
                creerPoint("Rabat", 0, depart, BigDecimal.ZERO),
                creerPoint("Marrakech", 1, depart.plusHours(3), new BigDecimal("120"))));

        Vehicule vehicule = new Vehicule();
        vehicule.setCapacite(4);
        Trajet trajet = new Trajet();
        TrajetResponse response = new TrajetResponse();

        when(vehiculeRepository.findById(2L)).thenReturn(Optional.of(vehicule));
        when(trajetMapper.toEntity(request)).thenReturn(trajet);
        when(trajetRepository.save(trajet)).thenReturn(trajet);
        when(trajetMapper.toResponse(trajet)).thenReturn(response);

        TrajetResponse result = trajetService.create(request);

        assertSame(response, result);
        assertSame(vehicule, trajet.getVehicule());
        assertEquals(StatutTrajet.PUBLIE, trajet.getStatut());
        assertEquals(depart, trajet.getDateDepart());
        assertEquals(2, trajet.getPointsArret().size());
        verify(trajetRepository).save(trajet);
        verify(trajetMapper).toResponse(trajet);
    }

    @Test
    void doitRetournerUnTrajetParSonIdentifiant() {
        Trajet trajet = new Trajet();
        TrajetResponse response = new TrajetResponse();

        when(trajetRepository.findById(1L)).thenReturn(Optional.of(trajet));
        when(trajetMapper.toResponse(trajet)).thenReturn(response);

        TrajetResponse result = trajetService.findById(1L);

        assertSame(response, result);
        verify(trajetRepository).findById(1L);
        verify(trajetMapper).toResponse(trajet);
    }

    @Test
    void doitRetournerLesTrajetsDUnConducteur() {
        Trajet trajet = new Trajet();
        TrajetResponse response = new TrajetResponse();

        when(trajetRepository.findByVehiculeConducteurId(1L)).thenReturn(List.of(trajet));
        when(trajetMapper.toResponse(trajet)).thenReturn(response);

        List<TrajetResponse> result = trajetService.findByConducteurId(1L);

        assertSame(response, result.get(0));
        verify(trajetRepository).findByVehiculeConducteurId(1L);
        verify(trajetMapper).toResponse(trajet);
    }

    private PointArretRequest creerPoint(
            String ville,
            int ordre,
            LocalDateTime heurePassage,
            BigDecimal prixDepuisDepart) {
        PointArretRequest point = new PointArretRequest();
        point.setVille(ville);
        point.setOrdre(ordre);
        point.setHeurePassage(heurePassage);
        point.setPrixDepuisDepart(prixDepuisDepart);
        return point;
    }
}
