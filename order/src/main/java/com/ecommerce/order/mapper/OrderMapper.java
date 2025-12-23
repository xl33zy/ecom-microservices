package com.ecommerce.order.mapper;

import com.ecommerce.order.dto.OrderItemDTO;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "items", source = "items")
    OrderResponse toOrderResponse(Order order);

    List<OrderItemDTO> mapOrderItems(List<OrderItem> items);

    @Mapping(target = "subTotal", expression = "java(item.getPrice().multiply(new java.math.BigDecimal(item.getQuantity())))")
    OrderItemDTO toOrderItemDTO(OrderItem item);

    @InheritInverseConfiguration
    @Mapping(target = "order", ignore = true)
    OrderItem toOrderItem(OrderItemDTO dto);
}
