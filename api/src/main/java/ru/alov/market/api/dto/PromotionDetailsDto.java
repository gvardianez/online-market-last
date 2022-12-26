package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Schema(description = "Модель доп. информации о новой акции")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionDetailsDto {

  @Schema(description = "Название акции", required = true, example = "Скидки")
  @NotBlank
  private String title;

  @Schema(description = "Описание акции", required = true, example = "Доп скидки на продукты...")
  @NotBlank
  private String description;

  @Schema(description = "Время начала акции", required = true, example = "2022-12-22 12:00:06")
  @FutureOrPresent
  private LocalDateTime startedAt;

  @Schema(description = "Время окончания акции", required = true, example = "2022-12-25 12:00:06")
  @Future
  private LocalDateTime endedAt;
}
