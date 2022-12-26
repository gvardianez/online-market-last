package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(description = "Модель элемента корзины")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {

  @Schema(description = "ID продукта", required = true, example = "1")
  private Long productId;

  @Schema(
      description = "Название продукта",
      required = true,
      maxLength = 255,
      minLength = 3,
      example = "Коробка конфет")
  private String productTitle;

  @Schema(description = "Количество продукта", required = true, example = "2")
  private int quantity;

  @Schema(description = "Цена единицы продукта", required = true, example = "120.00")
  private BigDecimal pricePerProduct;

  @Schema(description = "Общая цена продукта по количеству ", required = true, example = "240.00")
  private BigDecimal price;
}
