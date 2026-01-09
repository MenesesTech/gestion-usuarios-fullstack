package com.benconsult.user_management_api.users.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    // Maneja cuando un usuario no existe en el sistema
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException ex) {
        log.warn("Usuario no encontrado: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, "Usuario no encontrado", ex.getMessage());
    }

    // Maneja intentos de registro o actualización con datos duplicados
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        log.warn("Usuario duplicado: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Usuario ya registrado", ex.getMessage());
    }

    // Maneja errores relacionados con credenciales y contraseñas
    @ExceptionHandler({
            PasswordMismatchException.class,
            InvalidPasswordException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<?> handleCredentialErrors(RuntimeException ex) {
        log.warn("Error de autenticación: {}", ex.getMessage());
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Error de autenticación",
                ex.getMessage()
        );
    }

    // Maneja cualquier error no controlado del sistema
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        log.error("Error interno del servidor", ex);
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor",
                "Ocurrió un error inesperado"
        );
    }

    // Construye la respuesta estándar de error para la API
    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status,
            String error,
            String message
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);

        return new ResponseEntity<>(body, status);
    }
}

