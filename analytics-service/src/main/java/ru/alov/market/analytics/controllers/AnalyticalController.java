package ru.alov.market.analytics.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.alov.market.analytics.services.AnalyticalService;
import ru.alov.market.api.dto.ProductRatingDto;
import ru.alov.market.api.dto.RequestRatingDto;
import ru.alov.market.api.dto.UserProductsRatingDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/v1/analytical")
@RequiredArgsConstructor
@Tag(name = "Аналитика", description = "Методы для получения аналитических данных")
public class AnalyticalController {

  private final AnalyticalService analyticalService;

  @Operation(
      summary = "Запрос на получение аналитики по количеству проданных товаров за вчерашний день",
      responses = {
        @ApiResponse(
            description = "Успешный ответ",
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = ProductRatingDto.class)))
      })
  @GetMapping("/product-quantity-rating-yesterday")
  public Flux<ProductRatingDto> getYesterdayProductQuantityRating() {
    LocalDateTime localDateTimeStart =
        LocalDateTime.of(LocalDate.now().minusDays(1L), LocalTime.of(0, 0));
    LocalDateTime localDateTimeEnd = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0));
    return analyticalService.getProductQuantityRatingYesterday(
        localDateTimeStart, localDateTimeEnd);
  }

  @Operation(
      summary =
          "Запрос на получение аналитики по количеству проданных товаров за определенный период с возможностью вывода конкретного количества пунктов",
      responses = {
        @ApiResponse(
            description = "Успешный ответ",
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = ProductRatingDto.class)))
      })
  @PostMapping("/product-quantity-rating-period")
  public Flux<ProductRatingDto> getProductQuantityRatingForPeriod(
      @RequestBody RequestRatingDto requestRatingDto) {
    return analyticalService.getProductQuantityRatingPeriod(requestRatingDto);
  }

  @Operation(
      summary =
          "Запрос на получение аналитики по количеству проданных товаров и их общей стоимости(по каждой сумме по позиции товара) за определенный период с возможность вывода конкретного количества пунктов",
      responses = {
        @ApiResponse(
            description = "Успешный ответ",
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = ProductRatingDto.class)))
      })
  @PostMapping("/product-quantity-cost-rating-period")
  public Flux<ProductRatingDto> getProductQuantityAndCostRatingForPeriod(
      @RequestBody RequestRatingDto requestRatingDto) {
    return analyticalService.getProductQuantityAndCostRating(requestRatingDto);
  }

  @Operation(
      summary =
          "Запрос на получение аналитики для опреденного пользователя по количеству проданных товаров и их стоимости за указанный период",
      responses = {
        @ApiResponse(
            description = "Успешный ответ",
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = UserProductsRatingDto.class)))
      })
  @PostMapping("/user-products-rating-period")
  public Mono<UserProductsRatingDto> getUserProductsRatingForPeriod(
      @RequestBody RequestRatingDto requestRatingDto) {
    return analyticalService.getUserProductsStatistic(requestRatingDto);
  }
}
