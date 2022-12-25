package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Schema(description = "Модель запроса на обновление токенов доступа")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshJwtRequest {

  @Schema(
      description = "Токен обновления",
      example = "1323sadr23gfsdf23rwefedfasd",
      required = true)
  @NotBlank
  private String refreshToken;
}
