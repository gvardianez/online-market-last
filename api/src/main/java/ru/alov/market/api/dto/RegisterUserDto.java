package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Schema(description = "Модель для регистрации нового пользователя")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDto {

  @Schema(description = "Имя нового пользователя", example = "Alex", required = true)
  @NotBlank
  private String username;

  @Schema(description = "Пароль пользователя", example = "Df53fg^%%3f", required = true)
  @NotNull
  @Pattern(
      regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
      message =
          "Пароль должен содержать минимум одну цифру, хотя бы одну заглавную и одну прописную латинскую букву, хотя бы один спец символ(@#$%^&+=), не содержит пробелов и должен быть длиной не меннее 8 символов")
  private String password;

  @Schema(
      description = "Подтверждение нового пользователя",
      example = "Df53fg^%%3f",
      required = true)
  @NotNull
  @Pattern(
      regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
      message =
          "Пароль должен содержать минимум одну цифру, хотя бы одну заглавную и одну прописную латинскую букву, хотя бы один спец символ(@#$%^&+=), не содержит пробелов и должен быть длиной не меннее 8 символов")
  private String confirmPassword;

  @Schema(description = "Email пользователя", example = "Alex@ya.ru", required = true)
  @NotNull
  @Email
  private String email;
}
