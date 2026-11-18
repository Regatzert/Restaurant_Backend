package restaurant_backend.usuarios.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import jakarta.persistence.EntityManager;
import restaurant_backend.usuarios.entity.Rol;
import restaurant_backend.usuarios.entity.Usuario;

@Testcontainers
@SpringBootTest
@Transactional
class UsuarioRepositoryIntegrationTest {

    @Container
    static final PostgreSQLContainer postgres =
            new PostgreSQLContainer("postgres:17")
                    .withDatabaseName("restaurant_test")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void configurarDatasource(
            DynamicPropertyRegistry registry
    ) {
        registry.add(
                "spring.datasource.url",
                postgres::getJdbcUrl
        );

        registry.add(
                "spring.datasource.username",
                postgres::getUsername
        );

        registry.add(
                "spring.datasource.password",
                postgres::getPassword
        );
    }

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void deberiaEncontrarRolesInsertadosPorFlyway() {

        Optional<Rol> resultado =
                rolRepository.findByNombre("ADMIN");

        assertTrue(resultado.isPresent());

        assertEquals(
                "ADMIN",
                resultado.orElseThrow().getNombre()
        );

        assertTrue(
                resultado.orElseThrow().getActivo()
        );
    }

    @Test
    void deberiaGuardarUsuarioConSuRolEnPostgresql() {

        // Arrange

        Rol rol = rolRepository.findByNombre("ADMIN")
                .orElseThrow();

        Usuario usuario = Usuario.builder()
                .username("usuario_test")
                .password("HASH_DE_PRUEBA")
                .nombres("Usuario")
                .apellidos("Prueba")
                .email("usuario_test@email.com")
                .activo(true)
                .build();

        usuario.getRoles().add(rol);

        // Act

        Usuario usuarioGuardado =
                usuarioRepository.saveAndFlush(usuario);

        Long id = usuarioGuardado.getId();

        entityManager.clear();

        // Assert

        Usuario usuarioConsultado = usuarioRepository
                .findById(id)
                .orElseThrow();

        assertEquals(
                "usuario_test",
                usuarioConsultado.getUsername()
        );

        assertEquals(
                1,
                usuarioConsultado.getRoles().size()
        );

        assertEquals(
                "ADMIN",
                usuarioConsultado.getRoles()
                        .iterator()
                        .next()
                        .getNombre()
        );
    }
}