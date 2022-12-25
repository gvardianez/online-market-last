package ru.alov.market.messaging.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.alov.market.api.dto.NewsCreateDto;
import ru.alov.market.api.enums.RoleStatus;
import ru.alov.market.api.exception.CartServiceAppError;
import ru.alov.market.messaging.services.NewsService;

import javax.mail.MessagingException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
@Tag(name = "Новости", description = "Методы работы с рассылкой новостей")
public class NewsController {

  private final NewsService newsService;

  @Operation(
      summary = "Запрос на рассылку новости",
      responses = {
        @ApiResponse(description = "Успешный ответ", responseCode = "200"),
        @ApiResponse(
            description = "Недостаточно прав доступа",
            responseCode = "403",
            content = @Content(schema = @Schema(implementation = CartServiceAppError.class)))
      })
  @PostMapping
  public void sendNews(@RequestHeader String role, @RequestBody NewsCreateDto newsCreateDto)
      throws MessagingException, TelegramApiException {
    checkRole(role, List.of(RoleStatus.ROLE_ADMIN.toString()));
    newsService.sendAnyNews(newsCreateDto);
  }

  private void checkRole(String role, List<String> roleStatusList) {
    if (!roleStatusList.contains(role)) {
      throw new SecurityException("Недостаточно прав доступа");
    }
  }
}
