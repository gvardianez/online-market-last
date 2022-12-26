package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Schema(description = "Модель элемента(товара) акции")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionItemDto {

  @Schema(description = "ID элемента", required = true, example = "1")
  private Long id;

  @Schema(description = "ID акции в котором участвует данный товар", required = true, example = "3")
  @NotNull
  private Long promotionId;

  @Schema(description = "ID продукта", required = true, example = "3")
  @NotNull
  private Long productId;

  @Schema(description = "Скидка в %", required = true, example = "30.03", minimum = "0.01")
  @NotNull
  @Positive
  @DecimalMax("99.99")
  private float discount;

  public PromotionItemDto(Long productId, float discount) {
    this.productId = productId;
    this.discount = discount;
  }
}
