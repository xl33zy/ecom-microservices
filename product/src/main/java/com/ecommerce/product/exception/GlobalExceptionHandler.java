package com.ecommerce.product.exception;

import com.ecommerce.product.dto.ApiResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private String requestId() {
        return UUID.randomUUID().toString();
    }

    private String path(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<?>> handleProductNotFound(
            ProductNotFoundException ex,
            WebRequest request
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(ApiResponseDTO.error(
                                     HttpStatus.NOT_FOUND.value(),
                                     "PRODUCT_NOT_FOUND",
                                     ex.getMessage(),
                                     path(request),
                                     requestId(),
                                     null
                             ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<?>> handleGenericException(
            Exception ex,
            WebRequest request
    ) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(ApiResponseDTO.error(
                                     HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                     "PRODUCT_SERVICE_ERROR",
                                     "Internal product service error",
                                     path(request),
                                     requestId(),
                                     null
                             ));
    }
}
