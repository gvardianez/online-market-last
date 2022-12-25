package ru.alov.market.analytics.configs;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.alov.market.api.dto.OrderDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String kafkaServer;

  @Value("${spring.kafka.consumer.group-id}")
  private String kafkaGroupId;

  @Bean
  public ConsumerFactory<String, OrderDto> orderDtoConsumerFactory() {
    JsonDeserializer<OrderDto> deserializer = new JsonDeserializer<>(OrderDto.class);
    return new DefaultKafkaConsumerFactory<>(
        giveProperties(deserializer), new StringDeserializer(), deserializer);
  }

  @Bean
  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, OrderDto>>
      orderDtoContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, OrderDto> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(orderDtoConsumerFactory());
    return factory;
  }

  private Map<String, Object> giveProperties(JsonDeserializer<?> deserializer) {
    Map<String, Object> properties = new HashMap<>();

    deserializer.setRemoveTypeHeaders(false);
    deserializer.addTrustedPackages("*");
    deserializer.setUseTypeMapperForKey(true);

    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);

    return properties;
  }
}
