package restaurant_backend.usuarios.dto;

public record RolResponse(
    Long id,
    String nombre,
    String descripcio,
    Boolean activo
) {    
}
