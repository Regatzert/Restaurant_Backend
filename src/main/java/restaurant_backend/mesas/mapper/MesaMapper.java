package restaurant_backend.mesas.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import restaurant_backend.mesas.dto.MesaCreateRequest;
import restaurant_backend.mesas.dto.MesaResponse;
import restaurant_backend.mesas.dto.MesaUpdateRequest;
import restaurant_backend.mesas.entity.Mesa;

@Mapper (componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MesaMapper {

    MesaResponse toResponse(Mesa mesa);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    Mesa toEntity(MesaCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    void updateEntity(
            MesaUpdateRequest request,
            @MappingTarget Mesa mesa
    );

}
