package restaurant_backend.clientes.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import restaurant_backend.clientes.dto.ClienteCreateRequest;
import restaurant_backend.clientes.dto.ClienteResponse;
import restaurant_backend.clientes.dto.ClienteUpdateRequest;
import restaurant_backend.clientes.service.ClienteService;
import restaurant_backend.shared.dto.PageResponse;

import java.net.URI;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController 
@RequestMapping ("/api/v1/clientes")
@RequiredArgsConstructor 
public class ClienteController {
    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteResponse> crear(@Valid @RequestBody ClienteCreateRequest request) {
        ClienteResponse respuesta = clienteService.crear(request);

        URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(respuesta.id())
                        .toUri();
        
        return ResponseEntity.created(location).body(respuesta);
    }

    @GetMapping
    public ResponseEntity <PageResponse<ClienteResponse>> listar(
        @RequestParam(required = false) String termino, 
        @RequestParam(required = false) Boolean activo, 
        @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        
        PageResponse<ClienteResponse> respuesta = clienteService.listar(termino, activo, pageable);
        
        return ResponseEntity.ok(respuesta);
    }

    @PatchMapping ("/{id}")
    public ResponseEntity<ClienteResponse> actualizar(@PathVariable  Long id, @Valid @RequestBody ClienteUpdateRequest request){
        return ResponseEntity.ok(clienteService.actualizar(id, request));
    }

    @PatchMapping ("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable Long id){
        clienteService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
    
    
}
