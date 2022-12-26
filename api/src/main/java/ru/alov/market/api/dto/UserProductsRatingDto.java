package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "Модель рейтинга покупок товаров для конкретного пользователя")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProductsRatingDto {

  @Schema(description = "Модель профиля прользователя", required = true)
  @NotNull
  private UserProfileDto userProfileDto;

  @Schema(description = "Рейтинговый список товаров приобретенных пользователем", required = true)
  @NotNull
  private List<ProductRatingDto> productRatingDtoList;
}
