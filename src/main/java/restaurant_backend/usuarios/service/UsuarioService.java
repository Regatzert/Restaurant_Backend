package restaurant_backend.usuarios.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import restaurant_backend.shared.dto.PageResponse;
import restaurant_backend.usuarios.dto.UsuarioCreateRequest;
import restaurant_backend.usuarios.dto.UsuarioResponse;
import restaurant_backend.usuarios.dto.UsuarioUpdateRequest;

public interface UsuarioService {
    UsuarioResponse crear(UsuarioCreateRequest request);
    UsuarioResponse buscarPorId(Long id);
    // List<UsuarioResponse> listar();
    PageResponse<UsuarioResponse> listar(
        String termino,
        Boolean actrivo,
        Pageable pageable
    );
    UsuarioResponse actualizar(Long id, UsuarioUpdateRequest request);
    void desactivar (Long id);
}
