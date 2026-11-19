package restaurant_backend.clientes.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import restaurant_backend.clientes.dto.ClienteCreateRequest;
import restaurant_backend.clientes.dto.ClienteResponse;
import restaurant_backend.clientes.dto.ClienteUpdateRequest;
import restaurant_backend.clientes.entity.Cliente;

@Mapper (componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ClienteMapper {
    ClienteResponse toResponse(Cliente cliente);

    @Mapping (target = "id", ignore = true)
    @Mapping (target = "activo", ignore = true)
    @Mapping (target = "fechaCreacion", ignore = true)
    Cliente toEntity(ClienteCreateRequest request);

    @Mapping (target = "id", ignore = true)
    @Mapping (target = "documento", ignore = true)
    @Mapping (target = "activo", ignore = true)
    @Mapping (target = "fechaCreacion", ignore = true)
    void updateEntity(ClienteUpdateRequest request,  @MappingTarget Cliente cliente);
}
