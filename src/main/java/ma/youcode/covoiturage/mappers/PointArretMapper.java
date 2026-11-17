package ma.youcode.covoiturage.mappers;

import ma.youcode.covoiturage.dtos.responses.PointArretResponse;
import ma.youcode.covoiturage.entities.PointArret;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PointArretMapper {

    @Mapping(target = "trajetId", source = "trajet.id")
    PointArretResponse toResponse(PointArret pointArret);
}
