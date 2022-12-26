package ru.alov.market.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.alov.market.api.dto.RecoverPasswordRequestDto;
import ru.alov.market.api.dto.UserProfileDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Service
@Validated
@RequiredArgsConstructor
public class KafkaProducerService {

  private final KafkaTemplate<String, UserProfileDto> userProfileDtoKafkaTemplate;
  private final KafkaTemplate<String, RecoverPasswordRequestDto> recoverPasswordDtoKafkaTemplate;

  public void sendUserProfileDto(@NotBlank String topic, @Valid UserProfileDto userProfileDto) {
    userProfileDtoKafkaTemplate.send(topic, userProfileDto);
  }

  public void sendRecoverPasswordDto(
      @NotBlank String topic, @Valid RecoverPasswordRequestDto recoverPasswordRequestDto) {
    recoverPasswordDtoKafkaTemplate.send(topic, recoverPasswordRequestDto);
  }
}
