package ru.alov.market.auth.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.alov.market.api.dto.ChangePasswordRequestDto;
import ru.alov.market.api.dto.ListDto;
import ru.alov.market.api.dto.RecoverPasswordRequestDto;
import ru.alov.market.api.dto.UserProfileDto;
import ru.alov.market.api.enums.KafkaTopic;
import ru.alov.market.api.exception.AuthServiceAppError;
import ru.alov.market.api.exception.ResourceNotFoundException;
import ru.alov.market.auth.converters.UserConverter;
import ru.alov.market.auth.services.KafkaProducerService;
import ru.alov.market.auth.services.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
@Tag(name = "Личный кабинет", description = "Методы для работы в личном кабинете пользователя")
public class AccountController {

  private final UserService userService;
  private final UserConverter userConverter;
  private final KafkaProducerService kafkaProducerService;

  @Operation(
      summary = "Запрос на получение профиля пользователя по id",
      responses = {
        @ApiResponse(
            description = "Успешный ответ",
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = UserProfileDto.class))),
        @ApiResponse(
            description = "Пользователь не найден",
            responseCode = "404",
            content = @Content(schema = @Schema(implementation = AuthServiceAppError.class)))
      })
  @GetMapping()
  public Mono<UserProfileDto> enterAccount(@RequestHeader String username) {
    return userService.findMonoByUsername(username).map(userConverter::entityToDto);
  }

  @Operation(
      summary = "Запрос на изменение пароля пользователя",
      responses = {
        @ApiResponse(description = "Успешный ответ", responseCode = "200"),
        @ApiResponse(
            description = "Пользователь не найден",
            responseCode = "404",
            content = @Content(schema = @Schema(implementation = AuthServiceAppError.class))),
        @ApiResponse(
            description = "Введеные пароли не совпадают",
            responseCode = "404",
            content = @Content(schema = @Schema(implementation = AuthServiceAppError.class))),
        @ApiResponse(
            description = "Неверно указан старый пароль",
            responseCode = "404",
            content = @Content(schema = @Schema(implementation = AuthServiceAppError.class)))
      })
  @PutMapping("/change-password")
  public void changePassword(
      @RequestHeader String username,
      @RequestBody ChangePasswordRequestDto changePasswordRequestDto) {
    userService.changePassword(username, changePasswordRequestDto);
  }

  @Operation(
      summary = "Запрос на восстановление пароля",
      responses = {
        @ApiResponse(description = "Успешный ответ", responseCode = "200"),
        @ApiResponse(
            description = "Пользователь не найден",
            responseCode = "404",
            content = @Content(schema = @Schema(implementation = AuthServiceAppError.class))),
        @ApiResponse(
            description = "Для восстановления пароля необходимо подтвредить Email",
            responseCode = "404",
            content = @Content(schema = @Schema(implementation = AuthServiceAppError.class)))
      })
  @GetMapping("/recover-password")
  public void recoverPassword(@RequestHeader String username, @RequestHeader String email) {
    kafkaProducerService.sendRecoverPasswordDto(
        KafkaTopic.RECOVER_PASSWORD_DTO.toString(),
        new RecoverPasswordRequestDto(email, userService.recoverPassword(username)));
  }

  @Operation(
      summary = "Запрос на подтверждение Email",
      responses = {
        @ApiResponse(description = "Успешный ответ", responseCode = "200"),
        @ApiResponse(
            description = "Пользователь не найден",
            responseCode = "404",
            content = @Content(schema = @Schema(implementation = AuthServiceAppError.class)))
      })
  @GetMapping("/confirm-email")
  public void confirmEmail(@RequestHeader String username) {
    kafkaProducerService.sendUserProfileDto(
        KafkaTopic.CONFIRM_EMAIL.toString(),
        userConverter.entityToDto(
            userService
                .findByUsername(username)
                .orElseThrow(
                    () ->
                        new ResourceNotFoundException(
                            String.format("Пользователь '%s' не найден", username)))));
  }

  @Operation(
      summary = "Запрос на получение списка Email подписчиков",
      responses = {
        @ApiResponse(
            description = "Успешный ответ",
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = ListDto.class)))
      })
  @GetMapping("/subscribers")
  public ListDto<String> getSubscribersEmails(@RequestHeader String role) {
    return new ListDto<>(userService.getSubscribersEmails(role));
  }
}
