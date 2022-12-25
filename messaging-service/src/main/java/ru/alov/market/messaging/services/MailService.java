package ru.alov.market.messaging.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.alov.market.api.dto.*;
import ru.alov.market.api.enums.RoleStatus;
import ru.alov.market.messaging.integrations.AuthServiceIntegration;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class MailService {

  private final JavaMailSender sender;
  private final AuthServiceIntegration authServiceIntegration;

  public void sendRegistrationMail(@Valid UserProfileDto userProfileDto) throws MessagingException {
    sendMail(
        userProfileDto.getEmail(),
        "Регистрация успешно пройдена",
        "Имя пользователя "
            + userProfileDto.getUsername()
            + "\nперейдите по ссылке для подтверждения почты: "
            + "http://localhost:3000/market-front/#!/confirm_email/"
            + userProfileDto.getUsername()
            + "/"
            + userProfileDto.getEmail());
  }

  public void sendConfirmEmailMail(@Valid UserProfileDto userProfileDto) throws MessagingException {
    sendMail(
        userProfileDto.getEmail(),
        "Подтверждение почты",
        "\nперейдите по ссылке для подтверждения почты: "
            + "http://localhost:3000/market-front/#!/confirm_email/"
            + userProfileDto.getUsername()
            + "/"
            + userProfileDto.getEmail());
  }

  public void sendCheckOnOrderMail(@Valid OrderDto orderDto) throws MessagingException {
    sendMail(orderDto.getEmail(), "Заказ успешно оплачен", "Номер заказа: " + orderDto.getId());
  }

  public void sendRecoverPasswordMail(@Valid RecoverPasswordRequestDto recoverPasswordRequestDto)
      throws MessagingException {
    sendMail(
        recoverPasswordRequestDto.getEmail(),
        "Пароль успешно изменен",
        "Новый пароль: " + recoverPasswordRequestDto.getPassword());
  }

  public void sendNewsMessage(@Valid NewsCreateDto newsCreateDto) throws MessagingException {
    List<String> emails =
        authServiceIntegration.getSubscribersEmails(RoleStatus.ROLE_ADMIN.toString()).getContent();
    sendMail(emails.toArray(new String[0]), newsCreateDto.getSubject(), newsCreateDto.getMessage());
  }

  public void sendPromotionMessage(@Valid PromotionDto promotionDto) throws MessagingException {
    List<String> emails =
        authServiceIntegration.getSubscribersEmails(RoleStatus.ROLE_ADMIN.toString()).getContent();
    sendMail(emails.toArray(new String[0]), promotionDto.getTitle(), promotionDto.getDescription());
  }

  private void sendMail(String email, String subject, String text) throws MessagingException {
    MimeMessage message = sender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
    helper.setTo(email);
    helper.setSubject(subject);
    helper.setText(text, true);
    sender.send(message);
  }

  private void sendMail(String[] email, String subject, String text) throws MessagingException {
    MimeMessage message = sender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
    helper.setTo(email);
    helper.setSubject(subject);
    helper.setText(text, true);
    sender.send(message);
  }
}
