package ru.alov.market.core.converters;

import org.springframework.stereotype.Component;
import ru.alov.market.api.dto.OrderItemDto;
import ru.alov.market.core.entities.OrderItem;

@Component
public class OrderItemConverter {
  public OrderItemDto entityToDto(OrderItem o) {
    return new OrderItemDto(
        o.getProduct().getId(),
        o.getProduct().getTitle(),
        o.getQuantity(),
        o.getPricePerProduct(),
        o.getPrice());
  }
}
