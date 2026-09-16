package restaurant_backend.usuarios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import restaurant_backend.usuarios.entity.Usuario;

public interface UsuarioRepository extends JpaRepository <Usuario, Long>{
    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
    
}
