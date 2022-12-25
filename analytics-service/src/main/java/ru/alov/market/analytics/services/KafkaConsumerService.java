package ru.alov.market.analytics.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.alov.market.analytics.converters.AnalyticalConverter;
import ru.alov.market.analytics.repositories.AnalyticalRepository;
import ru.alov.market.api.dto.OrderDto;

import javax.validation.Valid;

@Service
@Validated
@RequiredArgsConstructor
public class KafkaConsumerService {

  private final AnalyticalRepository analyticalRepository;
  private final AnalyticalConverter analyticalConverter;
  private static final String ORDER_DTO_TOPIC = "ORDER_DTO";

  @KafkaListener(
      topics = ORDER_DTO_TOPIC,
      groupId = "orders_analytic.server",
      containerFactory = "orderDtoContainerFactory")
  public void listenerOrder(@Valid OrderDto orderDto) {
    analyticalRepository.saveAll(analyticalConverter.orderDtoToEntities(orderDto));
  }
}
