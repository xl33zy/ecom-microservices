package com.ecommerce.gateway;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponseDTO<T> {
    private boolean success;
    private T data;
    private String message;
    private String path;
    private String requestId;
    private ErrorDTO error;

    public ApiResponseDTO(boolean success, T data, String message, String path, String requestId) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.path = path;
        this.requestId = requestId;
        this.error = null;
    }

    public ApiResponseDTO(boolean success, T data, String message, String path, String requestId, ErrorDTO error) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.path = path;
        this.requestId = requestId;
        this.error = error;
    }

    public static <T> ApiResponseDTO<T> success(T data, String message, String path, String requestId) {
        return new ApiResponseDTO<>(true, data, message, path, requestId);
    }

    public static <T> ApiResponseDTO<T> error(int status, String code, String message, String path, String requestId, T data) {
        return new ApiResponseDTO<>(false, data, message, path, requestId, new ErrorDTO(status, code));
    }
}
