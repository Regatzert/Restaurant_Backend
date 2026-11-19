package restaurant_backend.clientes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import restaurant_backend.clientes.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{
    boolean existsByDocumentoIgnoreCase (String documento);

    @Query ("""
        SELECT c
        FROM Cliente c
        WHERE(
            :termino IS NULL
            OR LOWER(c.nombres) LIKE LOWER(CONCAT('%', :termino, '%'))
            OR LOWER(c.apellidos) LIKE LOWER(CONCAT('%', :termino, '%'))
            OR LOWER(c.documento) LIKE LOWER(CONCAT('%', :termino, '%'))
            OR LOWER(c.telefono) LIKE LOWER(CONCAT('%', :termino, '%'))
            OR LOWER(c.email) LIKE LOWER(CONCAT('%', :termino, '%'))
            )
        AND(
            :activo IS NULL
            OR c.activo = :activo
            )
        """)
    Page<Cliente> buscar(
        @Param ("termino") String termino,
        @Param ("activo") Boolean  activo,
        Pageable pageable
    );
    
}
