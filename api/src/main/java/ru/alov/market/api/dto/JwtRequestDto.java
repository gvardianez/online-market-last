package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Schema(description = "Модель запроса токена безопасности")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtRequestDto {

  @NotBlank
  @Schema(description = "Имя пользователя", required = true, example = "Ivan")
  private String username;

  @NotBlank
  @Schema(description = "Пароль пользователя пользователя", required = true, example = "1010")
  private String password;
}
