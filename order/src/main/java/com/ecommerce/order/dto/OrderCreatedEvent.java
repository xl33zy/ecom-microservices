package com.ecommerce.order.dto;

import com.ecommerce.order.model.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    @NotNull
    private Long orderId;

    @NotBlank
    private String userId;

    @NotNull
    private OrderStatus status;

    @NotEmpty
    private List<OrderItemDTO> items;

    @NotNull
    private BigDecimal totalAmount;

    @NotNull
    private LocalDateTime createdAt;
}
