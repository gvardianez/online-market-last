package ru.alov.market.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Модель заказа")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

  @Schema(description = "ID заказа", required = true, example = "1")
  private Long id;

  @NotEmpty
  @Schema(
      description = "Список элементов заказа",
      required = true,
      example = "1 Конфеты 2 100.00 200.00")
  private List<OrderItemDto> items;

  @NotNull
  @Positive
  @Schema(description = "Стоимость заказа", required = true, example = "500.00")
  private BigDecimal totalPrice;

  @NotBlank
  @Schema(description = "Имя заказчика", required = true, example = "Alex")
  private String userName;

  @NotNull
  @Email
  @Schema(description = "Email заказчика", required = true, example = "Alex@ya.ru")
  private String email;

  @NotBlank
  @Schema(description = "Адресс заказа", required = true, example = "Tula")
  private String address;

  @NotBlank
  @Size(min = 6)
  @Schema(description = "Телефон заказчика", required = true, example = "561461263", minLength = 6)
  private String phone;

  @NotBlank
  @Schema(description = "Статус заказа", required = true, example = "CREATED")
  private String status;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
