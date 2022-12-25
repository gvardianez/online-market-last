package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Schema(description = "Модель элемента заказа")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {

  @Schema(description = "ID продукта", required = true, example = "1")
  private Long productId;

  @NotBlank
  @Schema(
      description = "Название продукта",
      required = true,
      maxLength = 255,
      minLength = 3,
      example = "Коробка конфет")
  private String productTitle;

  @NotNull
  @Min(1)
  @Schema(description = "Количество продукта", required = true, example = "2")
  private int quantity;

  @Positive
  @Schema(description = "Цена единицы продукта", required = true, example = "120.00")
  private BigDecimal pricePerProduct;

  @Positive
  @Schema(description = "Общая цена продукта по количеству ", required = true, example = "240.00")
  private BigDecimal price;
}
