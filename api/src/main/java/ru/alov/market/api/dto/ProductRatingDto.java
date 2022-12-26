package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(description = "Модель продуктового рейтинга")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRatingDto {

  @Schema(description = "Модель продукта", required = true)
  private ProductDto productDto;

  @Schema(description = "Количество приобретенных продуктов", required = true)
  private Integer quantity;

  @Schema(description = "Общая цена приобретенных продуктов")
  private BigDecimal cost;

  public ProductRatingDto(ProductDto productDto, Integer quantity) {
    this.productDto = productDto;
    this.quantity = quantity;
  }
}
