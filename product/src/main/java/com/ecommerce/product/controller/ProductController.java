package com.ecommerce.product.controller;

import com.ecommerce.product.dto.ApiResponseDTO;
import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.exception.EntityNotFoundException;
import com.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    private String requestId() {
        return UUID.randomUUID().toString();
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<ProductResponse>>> getAllProducts(WebRequest webRequest) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        productService.getAllProducts(),
                        "Products retrieved successfully",
                        path(webRequest),
                        requestId()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ProductResponse>> getProductById(
            @PathVariable String id,
            WebRequest webRequest
    ) {
        ProductResponse product = productService.getProductById(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        product,
                        "Product retrieved successfully",
                        path(webRequest),
                        requestId()
                )
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO<List<ProductResponse>>> searchProducts(
            @RequestParam String keyword,
            WebRequest webRequest
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        productService.searchProducts(keyword),
                        "Products search completed",
                        path(webRequest),
                        requestId()
                )
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<ProductResponse>> createProduct(
            @RequestBody ProductRequest request,
            WebRequest webRequest
    ) {
        ProductResponse created = productService.createProduct(request);

        return ResponseEntity.status(201).body(
                ApiResponseDTO.success(
                        created,
                        "Product created successfully",
                        path(webRequest),
                        requestId()
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ProductResponse>> updateProduct(
            @PathVariable String id,
            @RequestBody ProductRequest request,
            WebRequest webRequest
    ) {
        ProductResponse updated = productService.updateProduct(Long.valueOf(id), request)
                                                .orElseThrow(() -> new EntityNotFoundException("Product not found for update with id: " + id));

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        updated,
                        "Product updated successfully",
                        path(webRequest),
                        requestId()
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteProduct(
            @PathVariable String id,
            WebRequest webRequest
    ) {
        boolean deleted = productService.deleteProduct(Long.valueOf(id));

        if (!deleted) {
            throw new EntityNotFoundException("Product not found with id: " + id);
        }

        String requestId = UUID.randomUUID().toString();
        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        null,
                        "Product deleted successfully",
                        webRequest.getDescription(false).replace("uri=", ""),
                        requestId
                )
        );
    }

    private String path(WebRequest webRequest) {
        return webRequest.getDescription(false).replace("uri=", "");
    }
}
