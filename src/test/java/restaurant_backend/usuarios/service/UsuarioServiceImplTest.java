package restaurant_backend.usuarios.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import restaurant_backend.shared.exception.BusinessConflictException;
import restaurant_backend.shared.exception.ResourceNotFoundException;
import restaurant_backend.usuarios.dto.UsuarioCreateRequest;
import restaurant_backend.usuarios.dto.UsuarioResponse;
import restaurant_backend.usuarios.entity.Rol;
import restaurant_backend.usuarios.entity.Usuario;
import restaurant_backend.usuarios.mapper.UsuarioMapper;
import restaurant_backend.usuarios.repository.RolRepository;
import restaurant_backend.usuarios.repository.UsuarioRepository;

@ExtendWith (MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock 
    private UsuarioRepository usuarioRepository;
    
    @Mock 
    private RolRepository rolRepository;

    @Mock 
    private UsuarioMapper usuarioMapper;

    @Mock 
    private PasswordEncoder passwordEncoder;

    @InjectMocks 
    private UsuarioServiceImpl usuarioService;

    private UsuarioCreateRequest request;
    private Rol rolAdmin;
    private UsuarioResponse usuarioResponse;
    
    @BeforeEach 
    void setUp(){
        request = new UsuarioCreateRequest(
            "kevin", 
            "Password123", 
            "Kevin", 
            "Salazar", 
            "kevin@email.com", 
            Set.of(1L));

        rolAdmin = Rol.builder()
                    .id(1L)
                    .nombre("Admin")
                    .descripcion("Administrador del sistema")
                    .activo(true)
                    .build();
        
        usuarioResponse = new UsuarioResponse(
            1L,
            "kevin",
            "Kevin",
            "Salazar",
            "kevin@email.com",
            true,
            null,
            Set.of()
        );
    }

    @Test 
    void crear_GuardarUsuarioCorrectamente(){
        // Arrange: preparar los comportamientos simulados
        when(usuarioRepository.existsByUsername("kevin")).thenReturn(false);
        when(usuarioRepository.existsByEmail("kevin@email.com")).thenReturn(false);
        when(rolRepository.findAllById(Set.of(1L))).thenReturn(List.of(rolAdmin));

        Usuario usuario = Usuario.builder()
                            .username("kevin")
                            .password("Password123")
                            .nombres("Kevin")
                            .apellidos("Salazar")
                            .email("kevin@email.com")
                            .build();

        when(usuarioMapper.toEntity(request)).thenReturn(usuario);
        when(passwordEncoder.encode("Password123")).thenReturn("HASH_CIFRADO");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(usuarioMapper.toResponse(usuario)).thenReturn(usuarioResponse);

        // Act: ejecutar el método que estamos probando
        UsuarioResponse resultado = usuarioService.crear(request);

        // Assert: verificar los resultados
        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("kevin", resultado.username());
        assertEquals("kevin@email.com", resultado.email());
        assertEquals("HASH_CIFRADO", usuario.getPassword());

        verify(usuarioRepository).save(usuario);
        verify(passwordEncoder).encode("Password123");
    }

    @Test 
    void crear_lanzarExcepcionCuandoUsernameExiste(){
        //Arrange
        when(usuarioRepository.existsByUsername("kevin")).thenReturn(true);

        //Act + Assert
        assertThrows(
            BusinessConflictException.class, 
            () -> usuarioService.crear(request));

        verify(usuarioRepository, never()).save(any());
        verifyNoInteractions(rolRepository);
        verifyNoInteractions(passwordEncoder);
    }

    @Test 
    void crear_LanzarExcepcionCuandoEmailExiste(){
        //Arrange
        when(usuarioRepository.existsByUsername("kevin")).thenReturn(false);
        when(usuarioRepository.existsByEmail("kevin@email.com")).thenReturn(true);

        //Act + Assert
        assertThrows(
            BusinessConflictException.class, 
            () -> usuarioService.crear(request));
        
        verify(usuarioRepository, never()).save(any());
        verifyNoInteractions(rolRepository);
        verifyNoInteractions(passwordEncoder);
    }

    @Test 
    void crear_LanzarExcepcionCuandoRolNoExiste(){
        //Arrange
        when(usuarioRepository.existsByUsername("kevin")).thenReturn(false);
        when(usuarioRepository.existsByEmail("kevin@email.com")).thenReturn(false);
        when(rolRepository.findAllById(Set.of(1L))).thenReturn(List.of());

        //Act + Assert
        assertThrows(
            ResourceNotFoundException.class, 
            () -> usuarioService.crear(request));

        verify(usuarioRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test 
    void crear_LanzarExcepcionCuandoRolEstaInactivo(){
        //Arrange
        rolAdmin.setActivo(false);

        when(usuarioRepository.existsByUsername("kevin")).thenReturn(false);
        when(usuarioRepository.existsByEmail("kevin@email.com")).thenReturn(false);
        when(rolRepository.findAllById(Set.of(1L))).thenReturn(List.of(rolAdmin));

        //Act + Assert
        assertThrows(
            BusinessConflictException.class, 
            () -> usuarioService.crear(request));

        verify(usuarioRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test 
    void buscarPorId_LanzarExcepcionCuandoUsuarioNoExiste(){
        //Arrange
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        //Act + Assert
        assertThrows(
            ResourceNotFoundException.class, 
            () -> usuarioService.buscarPorId(999L));

        verify(usuarioRepository).findById(999L);
        verifyNoInteractions(usuarioMapper);
    }

    @Test 
    void buscarPorId_RetornarUsuarioCuandoExiste(){
        //Arrange
        Usuario usuario = Usuario.builder()
                            .id(1L)
                            .username("kevin")
                            .nombres("Kevin")
                            .apellidos("Salazar")
                            .email("kevin@email.com")
                            .activo(true)
                            .build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toResponse(usuario)).thenReturn(usuarioResponse);

        //Act
        UsuarioResponse resultado = usuarioService.buscarPorId(1L);

        //Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("kevin", resultado.username());

        verify(usuarioRepository).findById(1L);
        verify(usuarioMapper).toResponse(usuario);
    }

    @Test
void crear_DeberiaGuardarLaContrasenaCifrada() {

    // Arrange
    when(usuarioRepository.existsByUsername("kevin")).thenReturn(false);
    when(usuarioRepository.existsByEmail("kevin@email.com")).thenReturn(false);
    when(rolRepository.findAllById(Set.of(1L))).thenReturn(List.of(rolAdmin));

    Usuario usuario = Usuario.builder()
            .username("kevin")
            .password("Password123")
            .nombres("Kevin")
            .apellidos("Salazar")
            .email("kevin@email.com")
            .build();

    when(usuarioMapper.toEntity(request)).thenReturn(usuario);
    when(passwordEncoder.encode("Password123")).thenReturn("HASH_CIFRADO");
    when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
    when(usuarioMapper.toResponse(usuario)).thenReturn(usuarioResponse);

    // Act
    usuarioService.crear(request);

    // Assert
    ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);

    verify(usuarioRepository).save(captor.capture());
    Usuario usuarioGuardado = captor.getValue();
    assertEquals("HASH_CIFRADO", usuarioGuardado.getPassword());

    assertNotEquals(
            "Password123",
            usuarioGuardado.getPassword()
    );
}

}
