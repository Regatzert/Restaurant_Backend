package restaurant_backend.usuarios.service;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import restaurant_backend.shared.dto.PageResponse;
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

    // CREAR USUARIO
    @Override
    public UsuarioResponse crear(UsuarioCreateRequest request) {

        String username = normalizarUsername(request.username());
        String email = normalizarEmail(request.email());

        validarUsernameDisponible(username);
        validarEmailDisponible(email);

        Set<Rol> roles = obtenerRoles(request.rolesIds());

        Usuario usuario = usuarioMapper.toEntity(request);

        usuario.setUsername(username);
        usuario.setEmail(email);
        usuario.setPassword(
                passwordEncoder.encode(request.password())
        );
        usuario.setRoles(roles);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return usuarioMapper.toResponse(usuarioGuardado);
    }

    // BUSCAR USUARIO POR ID
    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {

        Usuario usuario = obtenerUsuario(id);

        return usuarioMapper.toResponse(usuario);
    }

    // LISTAR, BUSCAR Y PAGINAR USUARIOS
    @Override
    @Transactional(readOnly = true)
    public PageResponse<UsuarioResponse> listar(
            String termino,
            Boolean activo,
            Pageable pageable
    ) {

        String terminoNormalizado =
                (termino == null || termino.isBlank())
                        ? null
                        : termino.trim();

        Page<Usuario> usuarios = usuarioRepository.buscar(
                terminoNormalizado,
                activo,
                pageable
        );

        Page<UsuarioResponse> respuestas =
                usuarios.map(usuarioMapper::toResponse);

        return PageResponse.from(respuestas);
    }

    // ACTUALIZAR USUARIO
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

        // Normalizar correo cuando se proporciona
        if (request.email() != null && !request.email().isBlank()) {
            usuario.setEmail(normalizarEmail(request.email()));
        }

        // Actualizar roles únicamente cuando se envían
        if (request.rolesIds() != null) {

            Set<Rol> roles = obtenerRoles(request.rolesIds());

            usuario.setRoles(roles);
        }

        Usuario usuarioActualizado =
                usuarioRepository.save(usuario);

        return usuarioMapper.toResponse(usuarioActualizado);
    }

    // DESACTIVAR USUARIO (ELIMINACIÓN LÓGICA)
    @Override
    public void desactivar(Long id) {

        Usuario usuario = obtenerUsuario(id);

        // Si ya está desactivado, no hacemos nada
        if (Boolean.FALSE.equals(usuario.getActivo())) {
            return;
        }

        usuario.setActivo(false);

        usuarioRepository.save(usuario);
    }

    // OBTENER USUARIO
    private Usuario obtenerUsuario(Long id) {

        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un usuario con ID: " + id
                        )
                );
    }

    // VALIDAR USERNAME DISPONIBLE
    private void validarUsernameDisponible(String username) {

        if (usuarioRepository.existsByUsernameIgnoreCase(username)) {

            throw new BusinessConflictException(
                    "El username ya está registrado: " + username
            );
        }
    }

    // VALIDAR EMAIL DISPONIBLE
    private void validarEmailDisponible(String email) {

        if (usuarioRepository.existsByEmailIgnoreCase(email)) {

            throw new BusinessConflictException(
                    "El correo electrónico ya está registrado: " + email
            );
        }
    }

    // VALIDAR EMAIL DURANTE ACTUALIZACIÓN
    private void validarEmailEnActualizacion(
            Usuario usuario,
            String email
    ) {

        if (email == null || email.isBlank()) {
            return;
        }

        String emailNormalizado = normalizarEmail(email);

        // Si es el mismo correo del usuario, no existe conflicto
        if (emailNormalizado.equalsIgnoreCase(usuario.getEmail())) {
            return;
        }

        boolean existe = usuarioRepository
                .existsByEmailIgnoreCaseAndIdNot(
                        emailNormalizado,
                        usuario.getId()
                );

        if (existe) {

            throw new BusinessConflictException(
                    "El correo electrónico ya está registrado"
            );
        }
    }

    // OBTENER Y VALIDAR ROLES
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

        boolean existeRolInactivo = rolesEncontrados.stream()
                .anyMatch(rol -> !Boolean.TRUE.equals(rol.getActivo()));

        if (existeRolInactivo) {

            throw new BusinessConflictException(
                    "No se pueden asignar roles inactivos"
            );
        }

        return new HashSet<>(rolesEncontrados);
    }

    // NORMALIZAR USERNAME
    private String normalizarUsername(String username) {

        return username.trim().toLowerCase(Locale.ROOT);
    }

    // NORMALIZAR EMAIL
    private String normalizarEmail(String email) {

        return email.trim().toLowerCase(Locale.ROOT);
    }
}
