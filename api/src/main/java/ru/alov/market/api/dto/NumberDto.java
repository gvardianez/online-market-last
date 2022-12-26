package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Schema(description = "Модель любого числа")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NumberDto<T extends Number> {

  @Schema(description = "Оборачиваемое в модель число", example = "1.23", required = true)
  @NotNull
  private T number;
}
