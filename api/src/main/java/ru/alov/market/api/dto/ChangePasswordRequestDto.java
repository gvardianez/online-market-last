package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Schema(description = "Модель запроса смены пароля пользователя")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequestDto {

  @Schema(description = "Старый пароль", required = true, example = "asdr#2Fh465$")
  @NotBlank
  private String oldPassword;

  @Schema(description = "Новый пароль", required = true, example = "asDFr45jg%%#$")
  @NotNull
  @Pattern(
      regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
      message =
          "Пароль должен содержать минимум одну цифру, хотя бы одну заглавную и одну прописную латинскую букву, хотя бы один спец символ(@#$%^&+=), не содержит пробелов и должен быть длиной не меннее 8 символов")
  private String newPassword;

  @Schema(description = "Подтверждение нового пароля", required = true, example = "asDFr45jg%%#$")
  @NotNull
  @Pattern(
      regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
      message =
          "Пароль должен содержать минимум одну цифру, хотя бы одну заглавную и одну прописную латинскую букву, хотя бы один спец символ(@#$%^&+=), не содержит пробелов и должен быть длиной не меннее 8 символов")
  private String confirmNewPassword;
}
