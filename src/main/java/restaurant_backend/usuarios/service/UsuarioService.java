package restaurant_backend.usuarios.service;

import java.util.List;

import restaurant_backend.usuarios.dto.UsuarioCreateRequest;
import restaurant_backend.usuarios.dto.UsuarioResponse;
import restaurant_backend.usuarios.dto.UsuarioUpdateRequest;

public interface UsuarioService {
    UsuarioResponse crear(UsuarioCreateRequest request);

    UsuarioResponse buscarPorId(Long id);

    List<UsuarioResponse> listar();

    UsuarioResponse actualizar(
            Long id,
            UsuarioUpdateRequest request
    );
}
