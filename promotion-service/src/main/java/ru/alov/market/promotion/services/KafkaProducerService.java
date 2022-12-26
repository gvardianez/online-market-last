package ru.alov.market.promotion.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.alov.market.api.dto.PromotionDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Service
@Validated
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, PromotionDto> promotionDtoKafkaTemplate;

    public void sendPromotionDto(@NotBlank String topic, @Valid PromotionDto promotionDto) {
        promotionDtoKafkaTemplate.send(topic, promotionDto);
    }

}
