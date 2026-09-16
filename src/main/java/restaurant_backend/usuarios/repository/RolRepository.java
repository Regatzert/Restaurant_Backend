package restaurant_backend.usuarios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import restaurant_backend.usuarios.entity.Rol;

public interface RolRepository  extends  JpaRepository<Rol, Long>{
    Optional<Rol> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
