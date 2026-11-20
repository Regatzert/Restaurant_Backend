package restaurant_backend.mesas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import restaurant_backend.mesas.entity.Mesa;

public interface MesaRepository extends JpaRepository<Mesa, Long>{
    boolean existsByNumero(Integer numero);
}
