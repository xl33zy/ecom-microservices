package com.ecommerce.user.exception;

import com.ecommerce.user.dto.ApiResponseDTO;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private String generateRequestId() {
        return UUID.randomUUID().toString();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleNotFound(EntityNotFoundException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(ApiResponseDTO.error(
                                     404,
                                     "NOT_FOUND",
                                     ex.getMessage(),
                                     path,
                                     generateRequestId(),
                                     null
                             ));
    }

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleDuplicate(DuplicateEntityException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body(ApiResponseDTO.error(
                                     409,
                                     "DUPLICATE_ENTITY",
                                     ex.getMessage(),
                                     path,
                                     generateRequestId(),
                                     null
                             ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleValidation(MethodArgumentNotValidException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(ApiResponseDTO.error(
                                     400,
                                     "VALIDATION_ERROR",
                                     message,
                                     path,
                                     generateRequestId(),
                                     null
                             ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(ApiResponseDTO.error(
                                     400,
                                     "VALIDATION_ERROR",
                                     ex.getMessage(),
                                     path,
                                     generateRequestId(),
                                     null
                             ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleAll(Exception ex, WebRequest request) {
        ex.printStackTrace(); // можно логировать через logger
        String path = request.getDescription(false).replace("uri=", "");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(ApiResponseDTO.error(
                                     500,
                                     "INTERNAL_SERVER_ERROR",
                                     "Unexpected error occurred",
                                     path,
                                     generateRequestId(),
                                     null
                             ));
    }
}
