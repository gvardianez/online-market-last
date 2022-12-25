package ru.alov.market.cart.utils;

import lombok.Data;
import ru.alov.market.api.dto.ProductDto;
import ru.alov.market.api.exception.ResourceNotFoundException;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class Cart {

  @NotNull private List<CartItem> items;

  @NotNull @PositiveOrZero private BigDecimal totalPrice;

  public Cart() {
    this.items = new ArrayList<>();
    this.totalPrice = BigDecimal.ZERO;
  }

  public void add(ProductDto p) {
    for (CartItem item : items) {
      if (item.getProductId().equals(p.getId())) {
        item.incrementQuantity();
        recalculate();
        return;
      }
    }
    CartItem cartItem = new CartItem(p.getId(), p.getTitle(), 1, p.getPrice(), p.getPrice());
    items.add(cartItem);
    recalculate();
  }

  public void merge(Cart guestCart) {
    guestCart.items.forEach(this::addCartItem);
    guestCart.clear();
  }

  public final void addCartItem(CartItem cartItem) {
    for (CartItem item : items) {
      if (item.getProductId().equals(cartItem.getProductId())) {
        item.setNewQuantity(item.getQuantity() + cartItem.getQuantity());
        recalculate();
        return;
      }
    }
    items.add(cartItem);
    recalculate();
  }

  public void changeProductQuantity(Long id, Integer delta) {
    for (CartItem item : items) {
      if (item.getProductId().equals(id)) {
        item.changeQuantity(delta);
        if (item.getQuantity() <= 0) items.remove(item);
        recalculate();
        return;
      }
    }
    throw new ResourceNotFoundException("Продукт не найден, id = " + id);
  }

  public void setProductQuantity(Long id, Integer newQuantity) {
    if (newQuantity == 0) {
      remove(id);
      return;
    }
    for (CartItem item : items) {
      if (item.getProductId().equals(id)) {
        item.setNewQuantity(newQuantity);
        recalculate();
        return;
      }
    }
    throw new ResourceNotFoundException("Продукт не найден, id = " + id);
  }

  public void remove(Long productId) {
    if (items.removeIf(p -> p.getProductId().equals(productId))) {
      recalculate();
    }
  }

  public void clear() {
    items.clear();
    totalPrice = BigDecimal.ZERO;
  }

  private void recalculate() {
    totalPrice = BigDecimal.ZERO;
    items.forEach(i -> totalPrice = totalPrice.add(i.getPrice()));
  }
}
