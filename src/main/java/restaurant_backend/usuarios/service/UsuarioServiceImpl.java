package restaurant_backend.usuarios.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import restaurant_backend.shared.exception.BusinessConflictException;
import restaurant_backend.shared.exception.ResourceNotFoundException;
import restaurant_backend.usuarios.dto.UsuarioCreateRequest;
import restaurant_backend.usuarios.dto.UsuarioResponse;
import restaurant_backend.usuarios.dto.UsuarioUpdateRequest;
import restaurant_backend.usuarios.entity.Rol;
import restaurant_backend.usuarios.entity.Usuario;
import restaurant_backend.usuarios.mapper.UsuarioMapper;
import restaurant_backend.usuarios.repository.RolRepository;
import restaurant_backend.usuarios.repository.UsuarioRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServiceImpl implements UsuarioService{
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UsuarioResponse crear(UsuarioCreateRequest request) {
        validarUsernameDisponible(request.username());
        validarEmailDisponible(request.email());
        Set<Rol> roles = obtenerRoles(request.rolesIds());
        
        Usuario usuario = usuarioMapper.toEntity(request);
        usuario.setPassword(
                passwordEncoder.encode(request.password())
        );
        usuario.setRoles(roles);
        Usuario usuarioGuardado =
                usuarioRepository.save(usuario);

        return usuarioMapper.toResponse(usuarioGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {
        Usuario usuario = obtenerUsuario(id);
        return usuarioMapper.toResponse(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {        
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::toResponse)
                .toList();
    }

    @Override
    public UsuarioResponse actualizar(
            Long id,
            UsuarioUpdateRequest request
    ) {
        Usuario usuario = obtenerUsuario(id);
        validarEmailEnActualizacion(
                usuario,
                request.email()
        );
        usuarioMapper.updateEntity(request, usuario);

        if (request.rolesIds() != null) {
            Set<Rol> roles = obtenerRoles(request.rolesIds());
            usuario.setRoles(roles);
        }

        Usuario usuarioActualizado =
                usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(usuarioActualizado);
    }

    private Usuario obtenerUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un usuario con ID: " + id
                        )
                );
    }

    private void validarUsernameDisponible(String username) {
        if (usuarioRepository.existsByUsername(username)) {
            throw new BusinessConflictException(
                    "El username ya está registrado: " + username
            );
        }
    }

    private void validarEmailDisponible(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new BusinessConflictException(
                    "El email ya está registrado: " + email
            );
        }
    }

    private void validarEmailEnActualizacion(
            Usuario usuario,
            String nuevoEmail
    ) {
        if (nuevoEmail == null ||
                nuevoEmail.equalsIgnoreCase(usuario.getEmail())) {
            return;
        }

        if (usuarioRepository.existsByEmail(nuevoEmail)) {
            throw new BusinessConflictException(
                    "El email ya está registrado: " + nuevoEmail
            );
        }
    }

    private Set<Rol> obtenerRoles(Set<Long> rolesIds) {
        if (rolesIds == null || rolesIds.isEmpty()) {
            throw new BusinessConflictException(
                    "El usuario debe tener al menos un rol"
            );
        }

        List<Rol> rolesEncontrados =
                rolRepository.findAllById(rolesIds);

        if (rolesEncontrados.size() != rolesIds.size()) {
            throw new ResourceNotFoundException(
                    "Uno o más roles no existen"
            );
        }
        return new HashSet<>(rolesEncontrados);
    }
}
