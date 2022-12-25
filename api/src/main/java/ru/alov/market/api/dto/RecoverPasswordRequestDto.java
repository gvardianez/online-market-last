package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Schema(description = "Модель запроса на восстановление пароля пользователем")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecoverPasswordRequestDto {

  @Schema(description = "Email пользователя", example = "Alex@ya.ru", required = true)
  @NotNull
  @Email
  private String email;

  @Schema(
      description = "Новый созданный временный пароль",
      example = "2312234",
      required = true,
      minLength = 6)
  @NotBlank
  @Size(min = 6)
  private String password;
}
