package ru.alov.market.analytics.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.alov.market.analytics.entities.Analytical;
import ru.alov.market.api.dto.OrderDto;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AnalyticalConverter {

  public List<Analytical> orderDtoToEntities(OrderDto orderDto) {
    List<Analytical> analyticalList = new ArrayList<>();
    orderDto
        .getItems()
        .forEach(
            orderItemDto ->
                analyticalList.add(
                    new Analytical(
                        orderItemDto.getProductId(),
                        orderDto.getId(),
                        orderDto.getUserName(),
                        orderItemDto.getQuantity(),
                        orderItemDto.getPricePerProduct(),
                        orderDto.getUpdatedAt())));
    return analyticalList;
  }
}
