package restaurant_backend.clientes.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table (name="clientes")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class Cliente {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false, length = 100)
    private String nombres;
    @Column (nullable = false, length = 100)
    private String apellidos;
    @Column (nullable = false, length = 20)
    private String documento;
    @Column (nullable = false, length = 20)
    private String telefono;
    @Column (length = 150)
    private String email;

    @Column (nullable = false)
    @Builder.Default
    private  Boolean activo = true;
    
    @Column (name = "fecha_creacion", nullable = false)
    @Builder.Default
    private LocalDateTime fechaCreacion = LocalDateTime.now();

}
