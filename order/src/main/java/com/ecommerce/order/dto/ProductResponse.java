package com.ecommerce.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponse {
    private Long id;
    private String name;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("stockQuantity")
    private Integer stockQuantity;
    private String category;
    private String imageUrl;
    private Boolean active;
}
