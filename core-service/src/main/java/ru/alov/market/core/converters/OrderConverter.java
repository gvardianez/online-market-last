package ru.alov.market.core.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.alov.market.api.dto.OrderDto;
import ru.alov.market.core.entities.Order;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderConverter {

  private final OrderItemConverter orderItemConverter;

  public OrderDto entityToDto(Order o) {
    return new OrderDto(
        o.getId(),
        o.getItems().stream().map(orderItemConverter::entityToDto).collect(Collectors.toList()),
        o.getTotalPrice(),
        o.getAddress(),
        o.getPhone(),
        o.getStatus(),
        o.getUsername(),
        o.getEmail(),
        o.getCreatedAt(),
        o.getUpdatedAt());
  }
}
