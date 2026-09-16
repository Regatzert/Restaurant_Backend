package restaurant_backend.usuarios.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateRequest (
    @Size(
                max = 100,
                message = "Los nombres no pueden superar los 100 caracteres"
        )
        String nombres,

        @Size(
                max = 100,
                message = "Los apellidos no pueden superar los 100 caracteres"
        )
        String apellidos,

        @Email(message = "El email no tiene un formato válido")
        @Size(
                max = 150,
                message = "El email no puede superar los 150 caracteres"
        )
        String email,

        Boolean activo,

        Set<Long> rolesIds
) {    
}
