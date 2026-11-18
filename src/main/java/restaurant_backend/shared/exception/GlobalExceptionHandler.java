package restaurant_backend.shared.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import restaurant_backend.shared.dto.ApiErrorResponse;

@RestControllerAdvice 
public class GlobalExceptionHandler {

    @ExceptionHandler (ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> manejarNoEncontrado(ResourceNotFoundException exception, HttpServletRequest request){
        return construirRespuesta(
            HttpStatus.NOT_FOUND, 
            exception.getMessage(), 
            request.getRequestURI(), 
            null);
    }

    @ExceptionHandler (BusinessConflictException.class)
    public ResponseEntity<ApiErrorResponse> manejarConclicto(BusinessConflictException exception, HttpServletRequest request){
        return  construirRespuesta(
            HttpStatus.CONFLICT, 
            exception.getMessage(), 
            request.getRequestURI(), 
            null);
    }

    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> manejarValidaciones(MethodArgumentNotValidException exception, HttpServletRequest request){
        Map<String, String> errores = new LinkedHashMap<>();
        
        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> 
                    errores.putIfAbsent(
                    error.getField(), 
                    error.getDefaultMessage()));

        return construirRespuesta(
            HttpStatus.BAD_REQUEST, 
            "Los datos enviados no son validos", 
            request.getRequestURI(), 
            errores);
    }

    @ExceptionHandler (HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> manejarJsonInvalido(HttpMessageNotReadableException exception, HttpServletRequest request){
        return construirRespuesta(
            HttpStatus.BAD_REQUEST, 
            "El cuerpo de la peticion contiene un JSON invalido o datos incompatibles", 
            request.getRequestURI(), 
            null);
    }

    @ExceptionHandler (DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> manejarIntegridad(DataIntegrityViolationException exception, HttpServletRequest request){
        return construirRespuesta(
            HttpStatus.CONFLICT, 
            "La operacion no puede completarse porque los datos incumplen una restriccion de la base de datos", 
            request.getRequestURI(), 
            null);
    }

    private ResponseEntity<ApiErrorResponse> construirRespuesta(
        HttpStatus status, 
        String message, 
        String path, 
        Map<String, 
        String> validationErrors){
        ApiErrorResponse response = new ApiErrorResponse(
            LocalDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            message,
            path,
            validationErrors
        );

        return  ResponseEntity.status(status).body(response);
    }
}
