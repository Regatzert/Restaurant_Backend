package restaurant_backend.usuarios.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record UsuarioResponse(
        Long id,
        String username,
        String nombres,
        String apellidos,
        String email,
        Boolean activo,
        LocalDateTime fechaCreacion,
        Set<RolResponse> roles
) {
}
