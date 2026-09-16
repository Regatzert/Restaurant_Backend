package restaurant_backend.usuarios.mapper;

import org.mapstruct.Mapper;

import restaurant_backend.usuarios.dto.RolResponse;
import restaurant_backend.usuarios.entity.Rol;

@Mapper(componentModel = "spring")
public interface RolMapper {
    RolResponse toResponse(Rol rol);
}
