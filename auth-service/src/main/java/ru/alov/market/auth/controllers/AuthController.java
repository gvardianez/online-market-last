package ru.alov.market.auth.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alov.market.api.dto.JwtRequestDto;
import ru.alov.market.api.dto.JwtResponseDto;
import ru.alov.market.api.dto.RefreshJwtRequest;
import ru.alov.market.api.exception.AuthServiceAppError;
import ru.alov.market.auth.services.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authenticate")
@Tag(name = "Авторизация", description = "Методы работы с аторизацией пользователя")
public class AuthController {

  private final AuthService authService;

  @Operation(
      summary = "Запрос на создание токена безопасности для пользователя",
      responses = {
        @ApiResponse(
            description = "Успешный ответ",
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
        @ApiResponse(
            description = "Пользователь не найден",
            responseCode = "404",
            content = @Content(schema = @Schema(implementation = AuthServiceAppError.class))),
        @ApiResponse(
            description = "Неверный пароль",
            responseCode = "404",
            content = @Content(schema = @Schema(implementation = AuthServiceAppError.class)))
      })
  @PostMapping()
  public JwtResponseDto login(@RequestBody JwtRequestDto authRequest) {
    return authService.getJwtTokens(authRequest);
  }

  @Operation(
      summary = "Запрос на обновление токенов безопасности для пользователя",
      responses = {
        @ApiResponse(
            description = "Успешный ответ",
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
        @ApiResponse(
            description = "Вы не найдены в системе. Пожалуйста, перезайдите",
            responseCode = "404",
            content = @Content(schema = @Schema(implementation = AuthServiceAppError.class))),
        @ApiResponse(
            description = "Неверный токен обновления. Пожалуйста перезайдите",
            responseCode = "400",
            content = @Content(schema = @Schema(implementation = AuthServiceAppError.class)))
      })
  @PostMapping("/refresh-tokens")
  public JwtResponseDto refreshTokens(@RequestBody RefreshJwtRequest refreshJwtRequest) {
    return authService.refreshJwtTokens(refreshJwtRequest.getRefreshToken());
  }
}
