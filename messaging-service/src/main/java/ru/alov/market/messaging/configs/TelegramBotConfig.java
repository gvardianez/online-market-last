package ru.alov.market.messaging.configs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@Data
public class TelegramBotConfig {

  @Value("${bot.name}")
  String botName;

  @Value("${bot.token}")
  String token;

  @Value("${bot.owner}")
  Long ownerId;

  @Value("${bot.chat}")
  Long chatId;

  @Bean
  TelegramBotsApi telegramBotsApi() throws TelegramApiException {
    return new TelegramBotsApi(DefaultBotSession.class);
  }
}
