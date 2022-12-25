package ru.alov.market.promotion.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.alov.market.api.dto.PromotionDto;
import ru.alov.market.promotion.entities.Promotion;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PromotionConverter {

    private final PromotionItemConverter promotionItemConverter;

    public PromotionDto entityToDto(Promotion promotion) {
        return new PromotionDto(promotion.getId(), promotion.getTitle(), promotion.getDescription(), promotion.getPromotionItems().stream()
                                                                                                              .map(promotionItemConverter::entityToDto)
                                                                                                              .collect(Collectors.toList()), promotion.getStartedAt(), promotion.getEndedAt());
    }

}
