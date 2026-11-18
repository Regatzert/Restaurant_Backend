package restaurant_backend.usuarios.controller;

import java.net.URI;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import restaurant_backend.shared.dto.PageResponse;
import restaurant_backend.usuarios.dto.UsuarioCreateRequest;
import restaurant_backend.usuarios.dto.UsuarioResponse;
import restaurant_backend.usuarios.dto.UsuarioUpdateRequest;
import restaurant_backend.usuarios.service.UsuarioService;
import org.springframework.data.domain.Sort;

@RestController 
@RequestMapping ("api/v1/usuarios")
@RequiredArgsConstructor 
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody UsuarioCreateRequest request) {
        UsuarioResponse response = usuarioService.crear(request);
        URI location = URI.create("/api/v1/usuarios/" + response.id());
        
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<UsuarioResponse>> listar(
        @RequestParam(required = false)
        String termino,
        @RequestParam(required = false)
        Boolean activo,
        @PageableDefault(
                size = 10,
                sort = "id",
                direction = Sort.Direction.DESC
        )
        Pageable pageable
    ) {

        PageResponse<UsuarioResponse> respuesta =
                usuarioService.listar(
                        termino,
                        activo,
                        pageable
                );

        return ResponseEntity.ok(respuesta);
    }
    
    @GetMapping ("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id){
        UsuarioResponse response = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping ("/{id}")
    public ResponseEntity<UsuarioResponse> actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateRequest resquest){
        UsuarioResponse response = usuarioService.actualizar(id, resquest);

        return  ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(
            @PathVariable Long id
    ) {

        usuarioService.desactivar(id);

        return ResponseEntity.noContent().build();
    }
}
