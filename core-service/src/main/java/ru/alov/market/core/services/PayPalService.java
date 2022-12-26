package ru.alov.market.core.services;

import com.paypal.orders.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.alov.market.api.exception.ResourceNotFoundException;
import ru.alov.market.core.entities.Order;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor
public class PayPalService {
  private final OrderService orderService;

  @Transactional
  public OrderRequest createOrderRequest(@NotNull Long orderId) {
    Order order =
        orderService
            .findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Заказ не найден"));

    if (!order.getStatus().equals(Order.OrderStatus.CREATED.toString()))
      throw new IllegalStateException("Неверный статус заказа:" + order.getStatus());

    OrderRequest orderRequest = new OrderRequest();
    orderRequest.checkoutPaymentIntent("CAPTURE");

    ApplicationContext applicationContext =
        new ApplicationContext()
            .brandName("Market")
            .landingPage("BILLING")
            .shippingPreference("SET_PROVIDED_ADDRESS");
    orderRequest.applicationContext(applicationContext);

    List<PurchaseUnitRequest> purchaseUnitRequests = new ArrayList<>();
    PurchaseUnitRequest purchaseUnitRequest =
        new PurchaseUnitRequest()
            .referenceId(orderId.toString())
            .description("Market Order")
            .amountWithBreakdown(
                new AmountWithBreakdown()
                    .currencyCode("USD")
                    .value(String.valueOf(order.getTotalPrice()))
                    .amountBreakdown(
                        new AmountBreakdown()
                            .itemTotal(
                                new Money()
                                    .currencyCode("USD")
                                    .value(String.valueOf(order.getTotalPrice())))))
            .items(
                order.getItems().stream()
                    .map(
                        orderItem ->
                            new Item()
                                .name(orderItem.getProduct().getTitle())
                                .unitAmount(
                                    new Money()
                                        .currencyCode("USD")
                                        .value(String.valueOf(orderItem.getPrice())))
                                .quantity(String.valueOf(orderItem.getQuantity())))
                    .collect(Collectors.toList()))
            .shippingDetail(
                new ShippingDetail()
                    .name(new Name().fullName(order.getUsername()))
                    .addressPortable(
                        new AddressPortable()
                            .addressLine1("123 Townsend St")
                            .addressLine2("Floor 6")
                            .adminArea2("San Francisco")
                            .adminArea1("CA")
                            .postalCode("94107")
                            .countryCode("US")));
    purchaseUnitRequests.add(purchaseUnitRequest);
    orderRequest.purchaseUnits(purchaseUnitRequests);
    return orderRequest;
  }
}
