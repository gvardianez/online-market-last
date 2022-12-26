package ru.alov.market.promotion.converters;

import org.springframework.stereotype.Component;
import ru.alov.market.api.dto.PromotionItemDto;
import ru.alov.market.promotion.entities.PromotionItem;

@Component
public class PromotionItemConverter {

    public PromotionItemDto entityToDto(PromotionItem promotionItem) {
        return new PromotionItemDto(promotionItem.getId(), promotionItem.getPromotion().getId(), promotionItem.getProductId(), promotionItem.getDiscount());
    }

}
