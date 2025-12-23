package com.ecommerce.product.service;

import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.exception.ProductNotFoundException;
import com.ecommerce.product.mapper.ProductMapper;
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
    private final ProductMapper productMapper;

    @Transactional
    public List<ProductResponse> getAllProducts() {
        return productRepository.findByActiveTrue()
                                .stream()
                                .map(productMapper::toResponse)
                                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponse getProductById(String id) {
        return productRepository.findByIdAndActiveTrue(Long.valueOf(id))
                                .map(productMapper::toResponse)
                                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    @Transactional
    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword)
                                .stream()
                                .map(productMapper::toResponse)
                                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    @Transactional
    public Optional<ProductResponse> updateProduct(Long id, ProductRequest updateProductRequest) {
        return productRepository.findById(id)
                                .map(existingProduct -> {
                                    productMapper.updateProductFromRequest(updateProductRequest, existingProduct);
                                    Product saved = productRepository.save(existingProduct);
                                    return productMapper.toResponse(saved);
                                });
    }

    @Transactional
    public boolean deleteProduct(Long id) {
        return productRepository.findById(id)
                                .map(product -> {
                                    product.setActive(false);
                                    productRepository.save(product);
                                    return true;
                                })
                                .orElse(false);
    }
}
