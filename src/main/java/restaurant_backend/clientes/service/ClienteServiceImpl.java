package restaurant_backend.clientes.service;

import java.util.Locale;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import restaurant_backend.clientes.dto.ClienteCreateRequest;
import restaurant_backend.clientes.dto.ClienteResponse;
import restaurant_backend.clientes.dto.ClienteUpdateRequest;
import restaurant_backend.clientes.entity.Cliente;
import restaurant_backend.clientes.mapper.ClienteMapper;
import restaurant_backend.clientes.repository.ClienteRepository;
import restaurant_backend.shared.dto.PageResponse;
import restaurant_backend.shared.exception.BusinessConflictException;
import restaurant_backend.shared.exception.ResourceNotFoundException;

@Service 
@RequiredArgsConstructor 
@Transactional 
public class ClienteServiceImpl implements ClienteService{
    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    
    // REGISTRAR CLIENTE
    @Override
    public ClienteResponse crear(ClienteCreateRequest request) {

        String documento = normalizarDocumento(request.documento());

        validarDocumentoDisponible(documento);

        Cliente cliente = clienteMapper.toEntity(request);

        cliente.setNombres(request.nombres().trim());
        cliente.setApellidos(request.apellidos().trim());
        cliente.setDocumento(documento);
        cliente.setTelefono(request.telefono().trim());
        cliente.setEmail(normalizarEmail(request.email()));

        Cliente clienteGuardado = clienteRepository.save(cliente);

        return clienteMapper.toResponse(clienteGuardado);
    }

    // BUSCAR CLIENTE POR ID
    @Override
    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {

        Cliente cliente = obtenerCliente(id);

        return clienteMapper.toResponse(cliente);
    }

    // LISTAR Y BUSCAR CLIENTES
    @Override
    @Transactional(readOnly = true)
    public PageResponse<ClienteResponse> listar(
            String termino,
            Boolean activo,
            Pageable pageable
    ) {

        String terminoNormalizado =
                (termino == null || termino.isBlank())
                        ? null
                        : termino.trim();

        Page<Cliente> clientes = clienteRepository.buscar(
                terminoNormalizado,
                activo,
                pageable
        );

        Page<ClienteResponse> respuestas =
                clientes.map(clienteMapper::toResponse);

        return PageResponse.from(respuestas);
    }

    // ACTUALIZAR CLIENTE
    @Override
    public ClienteResponse actualizar(
            Long id,
            ClienteUpdateRequest request
    ) {

        Cliente cliente = obtenerCliente(id);

        clienteMapper.updateEntity(request, cliente);

        // Normalizar los campos modificados
        if (request.nombres() != null) {
            cliente.setNombres(request.nombres().trim());
        }

        if (request.apellidos() != null) {
            cliente.setApellidos(request.apellidos().trim());
        }

        if (request.telefono() != null) {
            cliente.setTelefono(request.telefono().trim());
        }

        if (request.email() != null) {
            cliente.setEmail(normalizarEmail(request.email()));
        }

        Cliente clienteActualizado = clienteRepository.save(cliente);

        return clienteMapper.toResponse(clienteActualizado);
    }

    // DESACTIVAR CLIENTE
    @Override
    public void desactivar(Long id) {

        Cliente cliente = obtenerCliente(id);

        if (Boolean.FALSE.equals(cliente.getActivo())) {
            return;
        }

        cliente.setActivo(false);

        clienteRepository.save(cliente);
    }

    // OBTENER CLIENTE
    private Cliente obtenerCliente(Long id) {

        return clienteRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un cliente con ID: " + id
                        )
                );
    }

    // VALIDAR DOCUMENTO DISPONIBLE
    private void validarDocumentoDisponible(String documento) {

        if (clienteRepository.existsByDocumentoIgnoreCase(documento)) {

            throw new BusinessConflictException(
                    "El documento ya está registrado: " + documento
            );
        }
    }

    // NORMALIZAR DOCUMENTO
    private String normalizarDocumento(String documento) {

        return documento.trim().toUpperCase(Locale.ROOT);
    }

    // NORMALIZAR EMAIL
    private String normalizarEmail(String email) {

        if (email == null || email.isBlank()) {
            return null;
        }

        return email.trim().toLowerCase(Locale.ROOT);
    }
    
}
