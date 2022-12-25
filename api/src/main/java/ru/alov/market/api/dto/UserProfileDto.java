package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "Модель профиля пользователя")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {

  @Schema(description = "ID пользователя", required = true, example = "1")
  @NotNull
  private Long userId;

  @Schema(description = "Имя пользователя", required = true, example = "Alex")
  @NotBlank
  private String username;

  @Schema(description = "Email пользователя", required = true, example = "Alex@ya.ru")
  @NotNull
  @Email
  private String email;

  @Schema(description = "Список ролей пользователя", required = true, example = "ROLE_USER")
  @NotEmpty
  private List<String> roles;

  @Schema(description = "Статус Email", required = true, example = "MAIL_CONFIRMED")
  @NotBlank
  private String emailStatus;

  public UserProfileDto(String username, String email, List<String> roles) {
    this.username = username;
    this.email = email;
    this.roles = roles;
  }
}
