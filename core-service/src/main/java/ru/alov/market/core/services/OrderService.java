package ru.alov.market.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;
import ru.alov.market.api.dto.OrderDetailsDto;
import ru.alov.market.api.dto.RecalculateCartRequestDto;
import ru.alov.market.api.exception.ResourceNotFoundException;
import ru.alov.market.core.entities.Order;
import ru.alov.market.core.entities.OrderItem;
import ru.alov.market.core.integrations.CartServiceIntegration;
import ru.alov.market.core.integrations.PromotionServiceIntegration;
import ru.alov.market.core.repositories.OrderRepository;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class OrderService {

  private final CartServiceIntegration cartServiceIntegration;
  private final PromotionServiceIntegration promotionServiceIntegration;
  private final OrderRepository orderRepository;
  private final ProductService productService;

  @Transactional
  public Mono<Order> createNewOrder(
      @NotBlank String username,
      @NotNull @Email String email,
      @Valid OrderDetailsDto orderDetailsDto) {

    return cartServiceIntegration
        .getCurrentUserCart(username)
        .flatMap(
            cartDto -> {
              if (cartDto.getItems().isEmpty()) {
                return Mono.error(
                    new IllegalStateException("Нельзя оформить заказ для пустой корзины"));
              }
              return promotionServiceIntegration
                  .getRecalculateCart(new RecalculateCartRequestDto(cartDto, LocalDateTime.now()))
                  .map(
                      cartDto1 -> {
                        Order order = new Order();
                        if (orderDetailsDto.getEmail() == null) {
                          order.setEmail(email);
                        } else order.setEmail(orderDetailsDto.getEmail());
                        order.setTotalPrice(cartDto1.getTotalPrice());
                        order.setUsername(username);
                        order.setItems(new ArrayList<>());
                        order.setAddress(orderDetailsDto.getAddress());
                        order.setPhone(orderDetailsDto.getPhone());
                        Order finalOrder = order;
                        cartDto1
                            .getItems()
                            .forEach(
                                ci -> {
                                  OrderItem oi = new OrderItem();
                                  oi.setOrder(finalOrder);
                                  oi.setPrice(ci.getPrice());
                                  oi.setQuantity(ci.getQuantity());
                                  oi.setPricePerProduct(ci.getPricePerProduct());
                                  oi.setProduct(
                                      productService
                                          .findById(ci.getProductId())
                                          .orElseThrow(
                                              () ->
                                                  new ResourceNotFoundException(
                                                      "Product not found")));
                                  finalOrder.getItems().add(oi);
                                });
                        order.setStatus(Order.OrderStatus.CREATED.name());
                        order = orderRepository.save(order);
                        return order;
                      });
            })
        .doOnSuccess(order -> cartServiceIntegration.clearCart(username).subscribe());
  }

  @Transactional
  public Order changeOrderStatus(@NotNull Long id, @NotNull Order.OrderStatus orderStatus) {
    Order order =
        findById(id)
            .orElseThrow(
                () -> {
                  throw new ResourceNotFoundException("Order not found");
                });
    order.setStatus(orderStatus.name());
    return order;
  }

  public List<Order> findOrdersByProductIdAndUsernameAndOrderStatus(
      @NotBlank String username, @NotNull Long productId, @NotBlank String status) {
    return orderRepository.findOrdersByProductIdAndUsernameAndOrderStatus(
        username, productId, status);
  }

  public Optional<Order> findById(@NotNull Long id) {
    return orderRepository.findById(id);
  }

  public List<Order> findUserOrders(@NotBlank String username) {
    return orderRepository.findAllByUsername(username);
  }
}
