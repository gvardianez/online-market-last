package ru.alov.market.cart.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
  private Long productId;
  private String productTitle;
  private int quantity;
  private BigDecimal pricePerProduct;
  private BigDecimal price;

  public void incrementQuantity() {
    quantity++;
    price = price.add(pricePerProduct);
  }

  public void changeQuantity(int delta) {
    this.quantity += delta;
    calculateTotalPrice(quantity);
  }

  public void setNewQuantity(int newQuantity) {
    this.quantity = newQuantity;
    calculateTotalPrice(quantity);
  }

  private void calculateTotalPrice(int quantity) {
    this.price = pricePerProduct.multiply(BigDecimal.valueOf(quantity));
  }
}
