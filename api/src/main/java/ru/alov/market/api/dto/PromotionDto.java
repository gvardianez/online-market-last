package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Модель акции(скидки)")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionDto {

  @Schema(description = "ID акции(скидки)", required = true, example = "1")
  private Long id;

  @Schema(description = "Название акции", required = true, example = "Скидки")
  @NotBlank
  private String title;

  @Schema(description = "Описание акции", required = true, example = "Снижаем цены на продукты...")
  @NotBlank
  private String description;

  @Schema(description = "Список элементов акции(товаров)", required = true)
  @NotEmpty
  private List<PromotionItemDto> itemDtoListDto;

  @Schema(description = "Время начала акции", required = true, example = "2022-12-20 12:00:06")
  @NotNull
  private LocalDateTime startedAt;

  @Schema(description = "Время окончания акции", required = true, example = "2022-12-25 12:00:06")
  @NotNull
  private LocalDateTime endedAt;
}
