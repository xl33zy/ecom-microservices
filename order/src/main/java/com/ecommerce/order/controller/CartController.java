package com.ecommerce.order.controller;

import com.ecommerce.order.dto.ApiResponseDTO;
import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.service.CartService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    private String requestId() {
        return UUID.randomUUID().toString();
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<CartItem>>> getCart(
            @RequestHeader("X-User-ID") String userId,
            WebRequest webRequest
    ) {
        log.info("Fetching cart for userId={}", userId);

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        cartService.getCart(userId),
                        "Cart retrieved successfully",
                        path(webRequest),
                        requestId()
                )
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<Void>> addToCart(
            @RequestHeader("X-User-ID") String userId,
            @Valid @RequestBody CartItemRequest request,
            WebRequest webRequest
    ) {
        log.info("Adding product {} to cart for userId={}", request.getProductId(), userId);

        cartService.addToCart(userId, request);

        return ResponseEntity.status(201).body(
                ApiResponseDTO.success(
                        null,
                        "Product added to cart successfully",
                        path(webRequest),
                        requestId()
                )
        );
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<ApiResponseDTO<Void>> removeFromCart(
            @RequestHeader("X-User-ID") String userId,
            @PathVariable Long productId,
            WebRequest webRequest
    ) {
        log.info("Removing product {} from cart for userId={}", productId, userId);

        cartService.deleteItemFromCart(userId, productId);

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        null,
                        "Item removed from cart successfully",
                        path(webRequest),
                        requestId()
                )
        );
    }

    private String path(WebRequest webRequest) {
        return webRequest.getDescription(false).replace("uri=", "");
    }
}
