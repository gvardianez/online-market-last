package ru.alov.market.messaging.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.alov.market.messaging.configs.TelegramBotConfig;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;

@Slf4j
@Validated
@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

  private final TelegramBotConfig botConfig;
  private final TelegramBotsApi telegramBotsApi;

  @PostConstruct
  private void initApi() throws TelegramApiException {
    telegramBotsApi.registerBot(this);
  }

  @Override
  public String getBotUsername() {
    return botConfig.getBotName();
  }

  @Override
  public String getBotToken() {
    return botConfig.getToken();
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      String messageText = update.getMessage().getText();
      try {
        if (messageText.startsWith(Command.SEND_IN_CHANNEL.getCommand())
            && botConfig.getOwnerId().equals(update.getMessage().getChatId())) {
          String textToSend = messageText.substring(Command.SEND_IN_CHANNEL.getCommand().length());
          sendMessageInChannel(textToSend);
        } else {
          prepareAndSendMessage(
              update.getMessage().getChatId(),
              "Недостаточно прав доступа, или команда не поддерживается");
        }
      } catch (TelegramApiException e) {
        e.printStackTrace();
      }
    }
  }

  public void sendMessageInChannel(@NotBlank String message) throws TelegramApiException {
    prepareAndSendMessage(botConfig.getChatId(), message);
  }

  private void prepareAndSendMessage(long chatId, String textToSend) throws TelegramApiException {
    SendMessage message = new SendMessage();
    message.setChatId(String.valueOf(chatId));
    message.setText(textToSend);
    execute(message);
  }

  public enum Command {
    SEND_IN_CHANNEL("/sendInChannel ");

    private final String command;

    Command(String command) {
      this.command = command;
    }

    public String getCommand() {
      return command;
    }
  }
}
