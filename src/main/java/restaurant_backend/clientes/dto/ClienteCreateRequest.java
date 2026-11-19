package restaurant_backend.clientes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteCreateRequest(
    @NotBlank (message = "Los nombres son obligatorios")
    @Size (max = 100, message = "Los nombres no pueden superar 100 caracteres")
    String nombres,

    @NotBlank (message = "Los apellidos son obligatorios")
    @Size (max = 100, message = "Los apellidos no pueden superar 100 caracteres")
    String apellidos,

    @NotBlank (message = "El documento es obligatorio")
    @Size (max = 10, message = "El documento no puede superar 10 caracteres")
    String documento,
    
    @NotBlank (message = "El telefono obligatorio")
    @Size (max = 9, message = "El telefono no puede superar 9 caracteres")
    @Pattern (regexp = "^[0-9+()\\\\- ]+$", message = "El telefono contiene caracteres no validos")
    String telefono,
    
    @Email  (message = "El correo electronico no tiene un formato valido")
    @Size (max = 150, message = "El correo electronico no puede superar 150 caracteres")
    String email
) {    
}
