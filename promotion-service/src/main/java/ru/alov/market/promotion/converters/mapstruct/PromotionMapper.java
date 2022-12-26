package ru.alov.market.promotion.converters.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.alov.market.api.dto.PromotionDto;
import ru.alov.market.promotion.entities.Promotion;

@Mapper(uses = {PromotionItemMapper.class},componentModel = "spring")
public interface PromotionMapper {

    @Mapping(source = "promotion.promotionItems", target = "itemDtoListDto")
    PromotionDto entityToDto(Promotion promotion);

}