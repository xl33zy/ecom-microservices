package com.ecommerce.order.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponseWrapper<T> {
    private boolean success;
    private T data;
    private String message;
    private String path;
    private String requestId;
    private Object error;
}
