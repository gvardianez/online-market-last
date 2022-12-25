package ru.alov.market.cart.converters;

import org.springframework.stereotype.Component;
import ru.alov.market.api.dto.CartItemDto;
import ru.alov.market.cart.utils.CartItem;

@Component
public class CartItemConverter {
  public CartItemDto entityToDto(CartItem c) {
    return new CartItemDto(
        c.getProductId(),
        c.getProductTitle(),
        c.getQuantity(),
        c.getPricePerProduct(),
        c.getPrice());
  }
}
