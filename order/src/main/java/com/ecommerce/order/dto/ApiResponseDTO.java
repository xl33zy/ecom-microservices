package com.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDTO<T> {
    private boolean success;
    private T data;
    private String message;
    private String path;
    private String requestId;
    private ErrorDTO error;

    public static <T> ApiResponseDTO<T> success(
            T data,
            String message,
            String path,
            String requestId
    ) {
        return new ApiResponseDTO<>(
                true,
                data,
                message,
                path,
                requestId,
                null
        );
    }

    public static <T> ApiResponseDTO<T> error(
            int status,
            String code,
            String message,
            String path,
            String requestId,
            T data
    ) {
        return new ApiResponseDTO<>(
                false,
                data,
                message,
                path,
                requestId,
                new ErrorDTO(status, code)
        );
    }
}
