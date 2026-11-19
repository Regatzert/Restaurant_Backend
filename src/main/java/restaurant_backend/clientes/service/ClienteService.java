package restaurant_backend.clientes.service;

import org.springframework.data.domain.Pageable;

import restaurant_backend.clientes.dto.ClienteCreateRequest;
import restaurant_backend.clientes.dto.ClienteResponse;
import restaurant_backend.clientes.dto.ClienteUpdateRequest;
import restaurant_backend.shared.dto.PageResponse;

public interface ClienteService {
    ClienteResponse crear(ClienteCreateRequest request);
    ClienteResponse buscarPorId(Long id);

    PageResponse<ClienteResponse> listar(String termino, Boolean activo, Pageable pageable);
    ClienteResponse actualizar(Long id, ClienteUpdateRequest request);
    void desactivar (Long id);
}
