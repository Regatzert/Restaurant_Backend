package restaurant_backend.mesas.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record MesaUpdateRequest(
    @Positive (message = "El numero de mesa debe ser positivo")
    Integer numero,

    @Min (value = 1, message = "La capacidad debe ser minimo 1")
    Integer capacidad,

    @Size (max = 100, message = "La ubicacion no puede superar los 100 caracteres")
    String ubicacion
) {    
}
