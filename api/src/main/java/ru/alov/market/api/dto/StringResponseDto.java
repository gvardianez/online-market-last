package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Schema(description = "Модель текстового ответа")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StringResponseDto {

  @Schema(description = "Текстовый ответ", required = true, example = "wretgdfew")
  @NotNull
  private String value;
}
