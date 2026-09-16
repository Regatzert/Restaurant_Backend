package restaurant_backend.usuarios.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import restaurant_backend.usuarios.dto.UsuarioCreateRequest;
import restaurant_backend.usuarios.dto.UsuarioResponse;
import restaurant_backend.usuarios.entity.Usuario;
import restaurant_backend.usuarios.dto.UsuarioUpdateRequest;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        uses = RolMapper.class
)
public interface UsuarioMapper {
     UsuarioResponse toResponse(Usuario usuario);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "roles", ignore = true)
    Usuario toEntity(UsuarioCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(
            target = "activo",
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(
            target = "nombres",
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(
            target = "apellidos",
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(
            target = "email",
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    void updateEntity(
            UsuarioUpdateRequest request,
            @MappingTarget Usuario usuario
    );

}
