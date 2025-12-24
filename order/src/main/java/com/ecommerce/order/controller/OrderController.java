package com.ecommerce.order.controller;

import com.ecommerce.order.dto.ApiResponseDTO;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    private String requestId() {
        return UUID.randomUUID().toString();
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<OrderResponse>> createOrder(
            @RequestHeader("X-User-ID") String userId,
            WebRequest webRequest
    ) {
        log.info("Creating order for userId={}", userId);

        OrderResponse order = orderService.createOrder(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponseDTO.success(
                        order,
                        "Order created successfully",
                        path(webRequest),
                        requestId()
                )
        );
    }

    private String path(WebRequest webRequest) {
        return webRequest.getDescription(false).replace("uri=", "");
    }
}
