package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "Модель корзины для пересчета ее стоимости по акциям")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecalculateCartRequestDto {

  @Schema(description = "Модель козины", required = true)
  @NotNull
  private CartDto cartDto;

  @Schema(
      description = "Дата на которую производится пересчет",
      required = true,
      example = "2022-12-25 12:00:06")
  @NotNull
  @FutureOrPresent
  private LocalDateTime localDateTime;
}
