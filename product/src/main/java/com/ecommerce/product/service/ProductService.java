package com.ecommerce.product.service;

import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.exception.ProductNotFoundException;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<ProductResponse> getAllProducts() {
        return productRepository.findByActiveTrue()
                                .stream()
                                .map(this::mapToProductResponse)
                                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(String id) {
        return productRepository
                .findByIdAndActiveTrue(Long.valueOf(id))
                .map(this::mapToProductResponse)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + id
                        )
                );
    }

    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword).stream()
                                .map(this::mapToProductResponse)
                                .collect(Collectors.toList());
    }

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product();
        updateProductFromRequest(product, productRequest);
        Product savedProduct = productRepository.save(product);
        return mapToProductResponse(savedProduct);
    }

    public Optional<ProductResponse> updateProduct(Long id, ProductRequest updateProductRequest) {
        return productRepository.findById(id)
                                .map(existingProduct -> {
                                    updateProductFromRequest(existingProduct, updateProductRequest);
                                    productRepository.save(existingProduct);
                                    return mapToProductResponse(existingProduct);
                                });
    }

    public boolean deleteProduct(Long id) {
        return productRepository.findById(id).map(product -> {
            product.setActive(false);
            productRepository.save(product);
            return true;
        }).orElse(false);
    }

    private void updateProductFromRequest(Product product, ProductRequest productRequest) {
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());

        // Null-safe stock: если не передан, ставим "бесконечный" запас
        if (productRequest.getStockQuantity() == null) {
            product.setStockQuantity(Integer.MAX_VALUE);
        } else {
            product.setStockQuantity(productRequest.getStockQuantity());
        }

        product.setCategory(productRequest.getCategory());
        product.setImageUrl(productRequest.getImageUrl());
    }

    private ProductResponse mapToProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());

        // Null-safe stock при отдаче клиенту
        response.setStockQuantity(product.getStockQuantity() != null ? product.getStockQuantity() : Integer.MAX_VALUE);

        response.setCategory(product.getCategory());
        response.setImageUrl(product.getImageUrl());
        response.setActive(product.getActive());
        return response;
    }
}
