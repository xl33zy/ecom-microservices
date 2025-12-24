package com.ecommerce.order.mapper;

import com.ecommerce.order.dto.OrderCreatedEvent;
import com.ecommerce.order.dto.OrderItemDTO;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderEventMapper {

    OrderEventMapper INSTANCE = Mappers.getMapper(OrderEventMapper.class);

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "items", source = "items")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "totalAmount", source = "totalAmount")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "createdAt", source = "createdAt")
    OrderCreatedEvent toOrderCreatedEvent(Order order);

    List<OrderItemDTO> mapOrderItems(List<OrderItem> items);

    @Mapping(target = "subTotal", expression = "java(item.getPrice().multiply(new java.math.BigDecimal(item.getQuantity())))")
    OrderItemDTO toOrderItemDTO(OrderItem item);
}
