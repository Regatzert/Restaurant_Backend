package restaurant_backend.usuarios.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import restaurant_backend.usuarios.entity.Usuario;

public interface UsuarioRepository extends JpaRepository <Usuario, Long>{
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByUsernameIgnoreCase(String username);
    boolean existsByUsernameIgnoreCaseAndIdNot(String username, Long id);
    @Query (""" 
        SELECT u
        FROM Usuario u
        WHERE (
            :termino IS NULL
            OR LOWER(u.username) LIKE LOWER(CONCAT('%', :termino, '%'))
            OR LOWER(u.nombres) LIKE LOWER(CONCAT('%', :termino, '%'))
            OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :termino, '%'))
            OR LOWER(u.email) LIKE LOWER(CONCAT('%', :termino, '%'))
        )
        AND(
            :activo IS NULL
            OR u.activo = :activo
        )
    """)
    Page<Usuario> buscar(@Param("termino") String termino, @Param("activo") Boolean activo, Pageable pageable);
        boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
}    
