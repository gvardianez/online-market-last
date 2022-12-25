package ru.alov.market.promotion.utils;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.alov.market.api.dto.ProductDto;
import ru.alov.market.api.dto.PromotionItemDto;
import ru.alov.market.api.exception.ResourceNotFoundException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class PromotionItemsList {

    private List<PromotionItemDto> promotionItems;

    @PostConstruct
    public void init() {
        promotionItems = new ArrayList<>();
    }

    public void add(ProductDto productDto, Float discount) {
        for (PromotionItemDto promotionItem : promotionItems) {
            if (promotionItem.getProductId().equals(productDto.getId()))
                return;
        }
        promotionItems.add(new PromotionItemDto(productDto.getId(), discount));
    }

    public void setProductDiscount(Long productId, Float discount) {
        promotionItems.stream()
                      .filter(promotionItemDto -> promotionItemDto.getProductId().equals(productId))
                      .findFirst()
                      .ifPresentOrElse(promotionItemDto -> promotionItemDto.setDiscount(discount), () -> {
                          throw new ResourceNotFoundException("Не найден продукт для назначения скидки");
                      });
    }

    public void remove(Long productId) {
        promotionItems.removeIf(p -> p.getProductId().equals(productId));
    }

    public void clear() {
        promotionItems.clear();
    }


}
