package ma.youcode.covoiturage.mappers;

import ma.youcode.covoiturage.dtos.requests.ReservationRequest;
import ma.youcode.covoiturage.dtos.responses.ReservationResponse;
import ma.youcode.covoiturage.entities.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PointArretMapper.class)
public interface ReservationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "montantTotal", ignore = true)
    @Mapping(target = "dateReservation", ignore = true)
    @Mapping(target = "statut", ignore = true)
    @Mapping(target = "passager", ignore = true)
    @Mapping(target = "trajet", ignore = true)
    @Mapping(target = "pointMontee", ignore = true)
    @Mapping(target = "pointDescente", ignore = true)
    Reservation toEntity(ReservationRequest request);

    @Mapping(target = "pointMontee", source = "pointMontee")
    @Mapping(target = "pointDescente", source = "pointDescente")
    @Mapping(target = "passagerId", source = "passager.id")
    @Mapping(target = "trajetId", source = "trajet.id")
    ReservationResponse toResponse(Reservation reservation);
}
