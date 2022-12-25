package ru.alov.market.core.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.alov.market.api.dto.ReviewDto;
import ru.alov.market.api.exception.CoreServiceAppError;
import ru.alov.market.core.converters.ReviewConverter;
import ru.alov.market.core.services.ReviewService;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "Отзывы", description = "Методы работы с отзывами о товарах")
public class ReviewController {

  private final ReviewService reviewService;
  private final ReviewConverter reviewConverter;

  @Operation(
      summary = "Запрос на создание нового отзыва",
      responses = {
        @ApiResponse(description = "Отзыв успешно создан", responseCode = "201"),
        @ApiResponse(
            description = "Продукт не найден",
            responseCode = "404",
            content = @Content(schema = @Schema(implementation = CoreServiceAppError.class))),
        @ApiResponse(
            description = "Вы не приобретали у нас данный товар",
            responseCode = "400",
            content = @Content(schema = @Schema(implementation = CoreServiceAppError.class))),
        @ApiResponse(
            description = "Вы уже оставили отзыв да данный товар",
            responseCode = "400",
            content = @Content(schema = @Schema(implementation = CoreServiceAppError.class)))
      })
  @PostMapping
  public ReviewDto createReview(@RequestHeader String username, @RequestBody ReviewDto reviewDto) {
    return reviewConverter.entityToDto(reviewService.createReview(username, reviewDto));
  }
}
