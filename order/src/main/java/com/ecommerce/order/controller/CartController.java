package com.ecommerce.order.controller;

import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
 
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(@RequestHeader("X-User-ID") String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.getCart(userId));
    }

    @PostMapping
    public ResponseEntity<String> addToCart(@RequestHeader("X-User-ID") String userId, @RequestBody CartItemRequest request) {
        boolean isAddedToCart = cartService.addToCart(userId, request);
        return isAddedToCart ? ResponseEntity.status(HttpStatus.CREATED).body("Successfully added Products to Cart") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not able to complete the request | Product Out of Stock or User not found or Product not found");
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<String> removeFromCart(@RequestHeader("X-User-ID") String userId, @PathVariable Long productId) {
        boolean isItemDeleted =  cartService.deleteItemFromCart(userId, productId);
        return isItemDeleted ? ResponseEntity.status(HttpStatus.NO_CONTENT).body("Successfully deleted Item from Cart") :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found or Product not found");
    }
}
