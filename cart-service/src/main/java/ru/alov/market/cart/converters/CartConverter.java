package ru.alov.market.cart.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.alov.market.api.dto.CartDto;
import ru.alov.market.cart.utils.Cart;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartConverter {
  private final CartItemConverter cartItemConverter;

  public CartDto entityToDto(Cart c) {
    return new CartDto(
        c.getItems().stream().map(cartItemConverter::entityToDto).collect(Collectors.toList()),
        c.getTotalPrice());
  }
}
