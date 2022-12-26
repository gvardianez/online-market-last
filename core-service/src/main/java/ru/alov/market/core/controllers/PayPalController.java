package ru.alov.market.core.controllers;

import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.Order;
import com.paypal.orders.OrderRequest;
import com.paypal.orders.OrdersCaptureRequest;
import com.paypal.orders.OrdersCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.alov.market.api.enums.KafkaTopic;
import ru.alov.market.core.converters.OrderConverter;
import ru.alov.market.core.services.KafkaProducerService;
import ru.alov.market.core.services.OrderService;
import ru.alov.market.core.services.PayPalService;

import java.io.IOException;

@Controller
@RequestMapping("/api/v1/paypal")
@RequiredArgsConstructor
public class PayPalController {

  private final PayPalHttpClient payPalClient;
  private final OrderService orderService;
  private final PayPalService payPalService;
  private final KafkaProducerService kafkaProducerService;
  private final OrderConverter orderConverter;

  @PostMapping("/create/{orderId}")
  public ResponseEntity<?> createOrder(@PathVariable Long orderId) throws IOException {
    OrdersCreateRequest request = new OrdersCreateRequest();
    request.prefer("return=representation");
    request.requestBody(payPalService.createOrderRequest(orderId));
    HttpResponse<Order> response = payPalClient.execute(request);
    return new ResponseEntity<>(response.result().id(), HttpStatus.valueOf(response.statusCode()));
  }

  @PostMapping("/capture/{payPalId}")
  public ResponseEntity<?> captureOrder(@PathVariable String payPalId) throws IOException {
    OrdersCaptureRequest request = new OrdersCaptureRequest(payPalId);
    request.requestBody(new OrderRequest());

    HttpResponse<Order> response = payPalClient.execute(request);
    Order payPalOrder = response.result();
    if ("COMPLETED".equals(payPalOrder.status())) {
      long orderId = Long.parseLong(payPalOrder.purchaseUnits().get(0).referenceId());
      ru.alov.market.core.entities.Order order =
          orderService.changeOrderStatus(
              orderId, ru.alov.market.core.entities.Order.OrderStatus.PAID);
      kafkaProducerService.sendOrderDto(
          KafkaTopic.ORDER_DTO.toString(), orderConverter.entityToDto(order));
      return new ResponseEntity<>("Order completed!", HttpStatus.valueOf(response.statusCode()));
    }
    return new ResponseEntity<>(payPalOrder, HttpStatus.valueOf(response.statusCode()));
  }
}
