package com.ecommerce.product.service;

import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.exception.ProductNotFoundException;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public List<ProductResponse> getAllProducts() {
        return productRepository.findByActiveTrue()
                                .stream()
                                .map(this::mapToProductResponse)
                                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponse getProductById(String id) {
        return productRepository
                .findByIdAndActiveTrue(Long.valueOf(id))
                .map(this::mapToProductResponse)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    @Transactional
    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword)
                                .stream()
                                .map(this::mapToProductResponse)
                                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product();
        updateProductFromRequest(product, productRequest);
        Product savedProduct = productRepository.save(product);
        return mapToProductResponse(savedProduct);
    }

    @Transactional
    public Optional<ProductResponse> updateProduct(Long id, ProductRequest updateProductRequest) {
        return productRepository.findById(id)
                                .map(existingProduct -> {
                                    updateProductFromRequest(existingProduct, updateProductRequest);
                                    Product saved = productRepository.save(existingProduct);
                                    return mapToProductResponse(saved);
                                });
    }

    @Transactional
    public boolean deleteProduct(Long id) {
        return productRepository.findById(id).map(product -> {
            product.setActive(false);
            productRepository.save(product);
            return true;
        }).orElse(false);
    }

    private void updateProductFromRequest(Product product, ProductRequest request) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());

        if (request.getPrice() == null) {
            throw new IllegalStateException("Product price is missing");
        }
        product.setPrice(request.getPrice());

        product.setStockQuantity(request.getStockQuantity() != null ? request.getStockQuantity() : Integer.MAX_VALUE);

        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());
    }

    private ProductResponse mapToProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStockQuantity(product.getStockQuantity() != null ? product.getStockQuantity() : Integer.MAX_VALUE);
        response.setCategory(product.getCategory());
        response.setImageUrl(product.getImageUrl());
        response.setActive(product.getActive());
        return response;
    }
}
