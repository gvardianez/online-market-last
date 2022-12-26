package ru.alov.market.cart.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.alov.market.cart.integrations.CoreServiceIntegration;
import ru.alov.market.cart.utils.Cart;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.function.Consumer;

@Service
@Validated
@RequiredArgsConstructor
public class CartService {

  private final CoreServiceIntegration coreServiceIntegration;
  private final RedisTemplate<String, Object> redisTemplate;

  public Cart getCurrentCart(@NotBlank String cartId) {
    if (!redisTemplate.hasKey(cartId)) {
      Cart cart = new Cart();
      redisTemplate.opsForValue().set(cartId, cart);
    }
    return (Cart) redisTemplate.opsForValue().get(cartId);
  }

  public void addToCart(@NotBlank String cartId, @NotNull Long productId) {
    coreServiceIntegration
        .findById(productId)
        .subscribe(productDto -> execute(cartId, cart -> cart.add(productDto)));
  }

  public void removeFromCart(@NotBlank String cartId, @NotNull Long productId) {
    execute(cartId, cart -> cart.remove(productId));
  }

  public void changeProductQuantity(
      @NotBlank String cartId, @NotNull Long id, @NotNull Integer delta) {
    execute(cartId, cart -> cart.changeProductQuantity(id, delta));
  }

  public void setProductQuantity(
      @NotBlank String cartId, @NotNull Long id, @NotNull @PositiveOrZero Integer newQuantity) {
    execute(cartId, cart -> cart.setProductQuantity(id, newQuantity));
  }

  private void execute(String cartId, Consumer<Cart> action) {
    Cart cart = getCurrentCart(cartId);
    action.accept(cart);
    redisTemplate.opsForValue().set(cartId, cart);
  }

  public void mergeCart(@NotBlank String username, @NotBlank String guestCartId) {
    Cart guestCart = getCurrentCart(guestCartId);
    if (guestCart.getItems().isEmpty()) return;
    Cart userCart = getCurrentCart(username);
    userCart.merge(guestCart);
    updateCart(username, userCart);
    updateCart(guestCartId, guestCart);
  }

  public void updateCart(@NotBlank String cartId, @Valid Cart cart) {
    redisTemplate.opsForValue().set(cartId, cart);
  }

  public void clearCart(@NotBlank String cartId) {
    execute(cartId, Cart::clear);
  }
}
