package com.ecommerce.gateway;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorDTO {
    private int status;
    private String code;

    public ErrorDTO(int status, String code) {
        this.status = status;
        this.code = code;
    }
}