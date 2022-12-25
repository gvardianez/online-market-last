package ru.alov.market.core.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.alov.market.api.dto.OrderDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Service
@Validated
@RequiredArgsConstructor
public class KafkaProducerService {

  private final KafkaTemplate<String, OrderDto> orderDtoKafkaTemplate;

  public void sendOrderDto(@NotBlank String topic, @Valid OrderDto orderDto) {
    orderDtoKafkaTemplate.send(topic, orderDto);
  }
}
