package restaurant_backend.clientes.dto;

import java.time.LocalDateTime;

public record ClienteResponse(
    Long id,
    String nombres,
    String apellidos,
    String documento,
    String telefono,
    String email,
    Boolean activo,
    LocalDateTime fechaCreacion
) {
}
