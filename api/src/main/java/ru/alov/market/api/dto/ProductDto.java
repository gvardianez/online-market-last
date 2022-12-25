package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель продукта")
public class ProductDto {

  @Schema(description = "ID продукта", required = true, example = "1")
  private Long id;

  @NotBlank
  @Schema(
      description = "Название продукта",
      required = true,
      maxLength = 255,
      minLength = 3,
      example = "Коробка конфет")
  private String title;

  @NotNull
  @Positive
  @Schema(description = "Цена продукта", required = true, example = "120.00")
  private BigDecimal price;

  @NotBlank
  @Schema(description = "Категория продукта", required = true, example = "Еда")
  private String categoryTitle;
}
