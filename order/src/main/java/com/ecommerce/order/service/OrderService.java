package com.ecommerce.order.service;

import com.ecommerce.order.dto.OrderCreatedEvent;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.model.OrderStatus;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.mapper.OrderMapper;
import com.ecommerce.order.mapper.OrderEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final StreamBridge streamBridge;
    private final OrderMapper orderMapper;
    private final OrderEventMapper orderEventMapper;

    @Transactional
    public OrderResponse createOrder(String userId) {
        List<CartItem> cartItems = cartService.getCart(userId);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty for user: " + userId);
        }

        BigDecimal totalPrice = cartItems.stream()
                                         .map(CartItem::getPrice)
                                         .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);

        List<OrderItem> orderItems = cartItems.stream()
                                              .map(item -> new OrderItem(
                                                      null,
                                                      item.getProductId(),
                                                      item.getQuantity(),
                                                      item.getPrice(),
                                                      order
                                              ))
                                              .toList();
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(userId);

        OrderCreatedEvent event = orderEventMapper.toOrderCreatedEvent(savedOrder);
        streamBridge.send("createOrder-out-0", event);

        return orderMapper.toOrderResponse(savedOrder);
    }
}
