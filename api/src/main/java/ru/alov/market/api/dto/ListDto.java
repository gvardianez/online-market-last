package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "Модель списка данных")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListDto<T> {

  @NotNull
  @Schema(description = "Список данных", required = true)
  private List<T> content;
}
