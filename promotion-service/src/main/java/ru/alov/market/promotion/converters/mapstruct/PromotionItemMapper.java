package ru.alov.market.promotion.converters.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.alov.market.api.dto.PromotionItemDto;
import ru.alov.market.promotion.entities.PromotionItem;

@Mapper(componentModel = "spring")
public interface PromotionItemMapper {

    @Mapping(source = "promotionItem.id" , target = "id")
    @Mapping(source = "promotionItem.promotion.id", target = "promotionId")
    PromotionItemDto entityToDto (PromotionItem promotionItem);

}
