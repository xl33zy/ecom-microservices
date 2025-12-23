package com.ecommerce.order.exception;

import com.ecommerce.order.dto.ApiResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.UUID;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private String generateRequestId() {
        return UUID.randomUUID().toString();
    }

    private String extractPath(WebRequest webRequest, HttpServletRequest servletRequest) {
        if (webRequest != null) {
            return webRequest.getDescription(false).replace("uri=", "");
        } else if (servletRequest != null) {
            return servletRequest.getRequestURI();
        }
        return "N/A";
    }

    private String extractRequestId(HttpServletRequest servletRequest) {
        if (servletRequest != null && servletRequest.getHeader("X-Request-Id") != null) {
            return servletRequest.getHeader("X-Request-Id");
        }
        return generateRequestId();
    }

    private ResponseEntity<ApiResponseDTO<Void>> buildErrorResponse(
            HttpStatus status,
            String code,
            String message,
            WebRequest webRequest,
            HttpServletRequest servletRequest
    ) {
        return ResponseEntity.status(status).body(
                ApiResponseDTO.error(
                        status.value(),
                        code,
                        message,
                        extractPath(webRequest, servletRequest),
                        extractRequestId(servletRequest),
                        null
                )
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleNotFound(
            EntityNotFoundException ex,
            WebRequest request
    ) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage(), request, null);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleIllegalState(
            IllegalStateException ex,
            WebRequest request
    ) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex.getMessage(), request, null);
    }

    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleOutOfStock(
            OutOfStockException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.CONFLICT, "OUT_OF_STOCK", ex.getMessage(), null, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleGeneric(
            Exception ex,
            WebRequest request
    ) {
        log.error("Unhandled exception", ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "Unexpected error occurred", request, null);
    }
}
