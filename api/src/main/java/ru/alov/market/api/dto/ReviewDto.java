package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "Модель отзыва на товар")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

  @Schema(description = "ID отзыва", required = true, example = "1")
  private Long id;

  @Schema(description = "ID продукта в отзыве", required = true, example = "1")
  @NotNull
  private Long productId;

  @Schema(description = "Имя пользователя составившего отзыв", required = true, example = "Alex")
  @NotBlank
  private String username;

  @Schema(
      description = "Оценка товара",
      required = true,
      example = "4",
      minimum = "1",
      maximum = "5")
  @NotNull
  @Min(1)
  @Max(5)
  private Integer grade;

  @Schema(description = "Описание отзыва", required = true, example = "Замечательный товар")
  @NotBlank
  private String description;

  @Schema(description = "Дата создания отзыва", example = "2022-12-16 00:00:06")
  private LocalDateTime createdAt;
}
