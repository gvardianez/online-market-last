package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Schema(description = "Модель создания новости")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsCreateDto {

  @Schema(description = "Тема новости", required = true, example = "Новые продукты")
  @NotBlank
  private String subject;

  @Schema(description = "Описание новости", required = true, example = "У нас новые продукты...")
  @NotBlank
  private String message;

  public enum SubjectTypes {
    NEW_PRODUCTS("Новые продукты");

    private final String subject;

    SubjectTypes(String subject) {
      this.subject = subject;
    }

    public String getSubject() {
      return subject;
    }
  }
}
