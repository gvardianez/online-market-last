package ru.alov.market.messaging.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.alov.market.api.dto.OrderDto;
import ru.alov.market.api.dto.PromotionDto;
import ru.alov.market.api.dto.RecoverPasswordRequestDto;
import ru.alov.market.api.dto.UserProfileDto;
import ru.alov.market.messaging.telegram.TelegramBot;

import javax.mail.MessagingException;
import javax.validation.Valid;

@Service
@Validated
@RequiredArgsConstructor
public class KafkaConsumerService {

  private final MailService mailService;
  private final TelegramBot telegramBot;

  private static final String USER_PROFILE_DTO_TOPIC = "USER_PROFILE_DTO";
  private static final String PROMOTION_DTO_TOPIC = "PROMOTION_DTO";
  private static final String ORDER_DTO_TOPIC = "ORDER_DTO";
  private static final String RECOVER_PASSWORD_DTO_TOPIC = "RECOVER_PASSWORD_DTO";
  private static final String CONFIRM_EMAIL_TOPIC = "CONFIRM_EMAIL";

  @KafkaListener(
      topics = USER_PROFILE_DTO_TOPIC,
      groupId = "registration.server",
      containerFactory = "userProfileDtoContainerFactory")
  public void userDtoListener(@Valid UserProfileDto userProfileDto) throws MessagingException {
    mailService.sendRegistrationMail(userProfileDto);
  }

  @KafkaListener(
      topics = CONFIRM_EMAIL_TOPIC,
      groupId = "confirm-email.server",
      containerFactory = "userProfileDtoContainerFactory")
  public void confirmEmailListener(@Valid UserProfileDto userProfileDto) throws MessagingException {
    mailService.sendConfirmEmailMail(userProfileDto);
  }

  @KafkaListener(
      topics = ORDER_DTO_TOPIC,
      groupId = "order.server",
      containerFactory = "orderDtoContainerFactory")
  public void listenerOrder(@Valid OrderDto orderDto) throws MessagingException {
    mailService.sendCheckOnOrderMail(orderDto);
  }

  @KafkaListener(
      topics = RECOVER_PASSWORD_DTO_TOPIC,
      groupId = "recover-password.server",
      containerFactory = "recoverPasswordDtoContainerFactory")
  public void listenerRecoverPassword(@Valid RecoverPasswordRequestDto recoverPasswordRequestDto)
      throws MessagingException {
    mailService.sendRecoverPasswordMail(recoverPasswordRequestDto);
  }

  @KafkaListener(
      topics = PROMOTION_DTO_TOPIC,
      groupId = "promotion.server",
      containerFactory = "promotionDtoContainerFactory")
  public void listenerPromotion(@Valid PromotionDto promotionDto)
      throws MessagingException, TelegramApiException {
    mailService.sendPromotionMessage(promotionDto);
    telegramBot.sendMessageInChannel(
        promotionDto.getTitle() + "\n" + promotionDto.getDescription());
  }
}
