package restaurant_backend.mesas.dto;

import java.time.LocalDateTime;

import restaurant_backend.mesas.entity.EstadoMesa;

public record MesaResponse(
    Long id,
    Integer numero,
    Integer capacidad,
    EstadoMesa estado,
    LocalDateTime fechaCreacion
) {
}
