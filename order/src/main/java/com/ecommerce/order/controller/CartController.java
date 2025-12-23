package com.ecommerce.order.controller;

import com.ecommerce.order.dto.ApiResponseDTO;
import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.service.CartService;
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
            @RequestBody CartItemRequest request,
            WebRequest webRequest
    ) {
        cartService.addToCart(userId, request);

        return ResponseEntity.status(201).body(
                ApiResponseDTO.<Void>success(
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
        cartService.deleteItemFromCart(userId, productId);

        return ResponseEntity.ok(
                ApiResponseDTO.<Void>success(
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

