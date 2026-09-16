package restaurant_backend.usuarios.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioCreateRequest (
    @NotBlank(message = "El username es obligatorio")
        @Size(
                min = 4,
                max = 100,
                message = "El username debe tener entre 4 y 100 caracteres"
        )
        String username,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(
                min = 8,
                max = 100,
                message = "La contraseña debe tener entre 8 y 100 caracteres"
        )
        String password,

        @NotBlank(message = "Los nombres son obligatorios")
        @Size(
                max = 100,
                message = "Los nombres no pueden superar los 100 caracteres"
        )
        String nombres,

        @NotBlank(message = "Los apellidos son obligatorios")
        @Size(
                max = 100,
                message = "Los apellidos no pueden superar los 100 caracteres"
        )
        String apellidos,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato válido")
        @Size(
                max = 150,
                message = "El email no puede superar los 150 caracteres"
        )
        String email,

        Set<Long> rolesIds
) {    
}
