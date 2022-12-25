package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "Модель запроса получения продуктового рейтинга")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestRatingDto {

  @Schema(
      description = "Имя пользователя(в случае получения рейтинга по конкретному пользователю)",
      example = "Alex")
  private String username;

  @Schema(
      description = "Дата начала фотмирования рейтинга",
      example = "2022-12-16 00:00:06",
      required = true)
  @NotNull
  private LocalDateTime localDateTimeStart;

  @Schema(
      description = "Дата окончания формирования рейтинга",
      example = "2022-12-18 00:00:06",
      required = true)
  @NotNull
  private LocalDateTime localDateTimeEnd;

  @Schema(description = "Желаемое количество товаров в рейтинге", example = "5")
  private Long count;
}
