package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "Модель страницы")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDto<T> {

  @Schema(
      description = "Список элементов на странице",
      required = true,
      example = "1 Конфеты 100.00 Еда")
  private List<T> content;

  @Schema(description = "Номер на странице", required = true, example = "1")
  private int page;

  @Schema(description = "Заданное количество элементов на странице", required = true, example = "5")
  private int size;

  @Schema(
      description = "Фактическое количество элементво на странице",
      required = true,
      example = "2")
  private int numberOfElements;

  @Schema(description = "Общее количество элементов", required = true, example = "10")
  private long totalElements;

  @Schema(description = "Общее количество страниц", required = true, example = "3")
  private int totalPages;
}
