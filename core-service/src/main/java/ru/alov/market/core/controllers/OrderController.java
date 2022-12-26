package ru.alov.market.core.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.alov.market.api.dto.OrderDetailsDto;
import ru.alov.market.api.dto.OrderDto;
import ru.alov.market.api.exception.AppError;
import ru.alov.market.api.exception.ResourceNotFoundException;
import ru.alov.market.core.converters.OrderConverter;
import ru.alov.market.core.services.OrderService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Заказы", description = "Методы работы с заказами")
public class OrderController {

  private final OrderService orderService;
  private final OrderConverter orderConverter;

  @Operation(
      summary = "Запрос на получение списка заказов по имени пользователя",
      responses = {
        @ApiResponse(
            description = "Успешный ответ",
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = List.class))),
        @ApiResponse(
            description = "Пользователь не найден",
            responseCode = "404",
            content = @Content(schema = @Schema(implementation = AppError.class)))
      })
  @GetMapping
  public List<OrderDto> getUserOrders(@RequestHeader String username) {
    return orderService.findUserOrders(username).stream()
        .map(orderConverter::entityToDto)
        .collect(Collectors.toList());
  }

  @Operation(
      summary = "Запрос на создание нового заказа",
      responses = {@ApiResponse(description = "Заказ успешно создан", responseCode = "201")})
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<OrderDto> createNewOrder(
      @RequestHeader String username,
      @RequestHeader String email,
      @RequestBody OrderDetailsDto orderDetailsDto) {
    return orderService
        .createNewOrder(username, email, orderDetailsDto)
        .map(orderConverter::entityToDto);
  }

  @GetMapping("/{id}")
  public OrderDto getOrderById(@PathVariable Long id) {
    return orderConverter.entityToDto(
        orderService.findById(id).orElseThrow(() -> new ResourceNotFoundException("ORDER 404")));
  }
}
