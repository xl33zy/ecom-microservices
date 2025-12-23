package com.ecommerce.gateway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class FallbackController {

    @GetMapping("/fallback/products")
    public ResponseEntity<ApiResponseDTO<List<String>>> productsFallback(
            @RequestHeader(value = "X-Request-Id", required = false) String requestId
    ) {
        String path = "/fallback/products";
        List<String> fallbackMessage = Collections.singletonList(
                "Product service is unavailable, please try later"
        );

        ApiResponseDTO<List<String>> response = ApiResponseDTO.error(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "SERVICE_UNAVAILABLE",
                "Product service is unavailable, please try later",
                path,
                requestId != null ? requestId : "N/A",
                fallbackMessage
        );

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                             .body(response);
    }
}
