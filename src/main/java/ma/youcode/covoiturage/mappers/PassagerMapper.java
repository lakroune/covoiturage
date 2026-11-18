package ma.youcode.covoiturage.mappers;

import ma.youcode.covoiturage.dtos.requests.PassagerRequest;
import ma.youcode.covoiturage.dtos.requests.PassagerRequestUpdate;
import ma.youcode.covoiturage.dtos.responses.PassagerResponse;
import ma.youcode.covoiturage.entities.Passager;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PassagerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    Passager toEntity(PassagerRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    void updateEntity(PassagerRequestUpdate request, @MappingTarget Passager passager);

    PassagerResponse toResponse(Passager passager);
}
