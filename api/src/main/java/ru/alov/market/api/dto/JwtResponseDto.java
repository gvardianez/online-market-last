package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Модель токена безопасности")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponseDto {

  @Schema(
      description = "Строковое представление токена",
      required = true,
      example = "fsdffsdfsd.dfsdfsdfsdfdsfsdf.213wefwe3q2rwefserfwe")
  private String accessToken;

  private String refreshToken;
}
