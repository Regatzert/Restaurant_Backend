package restaurant_backend.usuarios.controller;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import restaurant_backend.shared.exception.GlobalExceptionHandler;
import restaurant_backend.usuarios.dto.UsuarioResponse;
import restaurant_backend.usuarios.service.UsuarioService;

@WebMvcTest(UsuarioController.class)
@Import(GlobalExceptionHandler.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsuarioService usuarioService;

    private UsuarioResponse crearRespuestaUsuario() {

        return new UsuarioResponse(
                1L,
                "kevin",
                "Kevin",
                "Salazar",
                "kevin@email.com",
                true,
                LocalDateTime.of(2026, 11, 18, 10, 30),
                Set.of()
        );
    }
    
}
