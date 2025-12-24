package com.ecommerce.order.service;

import com.ecommerce.order.clients.ProductServiceClient;
import com.ecommerce.order.dto.ApiResponseWrapper;
import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.dto.ProductResponse;
import com.ecommerce.order.exception.EntityNotFoundException;
import com.ecommerce.order.exception.OutOfStockException;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;

    @Transactional
    public List<CartItem> getCart(String userId) {
        return cartItemRepository.findByUserId(userId);
    }

    @Transactional
    public void addToCart(String userId, CartItemRequest request) {

        ApiResponseWrapper<ProductResponse> responseWrapper = productServiceClient
                .getProductDetails(request.getProductId().toString());
        ProductResponse product = responseWrapper.getData();

        if (product == null) {
            throw new EntityNotFoundException(
                    "Product not found: " + request.getProductId()
            );
        }

        if (product.getStockQuantity() == null || product.getStockQuantity() < request.getQuantity()) {
            throw new OutOfStockException(request.getProductId());
        }

        if (product.getPrice() == null) {
            throw new IllegalStateException("Product price is missing for productId=" + request.getProductId());
        }

        saveCartItem(userId, request, product);
    }

    @Transactional
    public void saveCartItem(
            String userId,
            CartItemRequest request,
            ProductResponse product
    ) {
        CartItem cartItem = cartItemRepository
                .findByUserIdAndProductId(userId, request.getProductId());

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        }

        if (product.getPrice() == null) {
            throw new IllegalStateException("Product price is missing");
        }

        cartItem.setPrice(product.getPrice());
        cartItemRepository.save(cartItem);
    }

    @Transactional
    public void deleteItemFromCart(String userId, Long productId) {
        log.info("DELETE cart item: userId={}, productId={}", userId, productId);
        CartItem cartItem = cartItemRepository
                .findByUserIdAndProductId(userId, productId);

        if (cartItem == null) {
            throw new EntityNotFoundException(
                    "Cart item not found for productId=" + productId
            );
        }

        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
