package ru.alov.market.promotion.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;
import ru.alov.market.api.dto.CartDto;
import ru.alov.market.api.dto.CartItemDto;
import ru.alov.market.api.dto.PromotionDetailsDto;
import ru.alov.market.api.exception.FieldValidationException;
import ru.alov.market.promotion.entities.Promotion;
import ru.alov.market.promotion.entities.PromotionItem;
import ru.alov.market.promotion.integrations.CoreServiceIntegration;
import ru.alov.market.promotion.repositories.PromotionItemsRepository;
import ru.alov.market.promotion.repositories.PromotionRepository;
import ru.alov.market.promotion.repositories.projections.ProductDiscount;
import ru.alov.market.promotion.utils.PromotionItemsList;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionItemsList promotionItemsList;
    private final CoreServiceIntegration coreServiceIntegration;
    private final PromotionRepository promotionRepository;
    private final PromotionItemsRepository promotionItemsRepository;

    @Transactional
    public Promotion createNewPromotion(@Valid PromotionDetailsDto promotionDetailsDto) {
        Promotion promotion = new Promotion();
        promotion.setTitle(promotionDetailsDto.getTitle());
        promotion.setDescription(promotionDetailsDto.getDescription());
        promotion.setStartedAt(promotionDetailsDto.getStartedAt());
        promotion.setEndedAt(promotionDetailsDto.getEndedAt());
        Promotion finalPromotion = promotion;
        List<PromotionItem> promotionItems = promotionItemsList.getPromotionItems()
                                                               .stream()
                                                               .map(promotionItemDto -> new PromotionItem(finalPromotion, promotionItemDto.getProductId(), promotionItemDto.getDiscount()))
                                                               .collect(Collectors.toList());
        promotion.setPromotionItems(promotionItems);
        promotion = promotionRepository.save(promotion);
        clearNewPromotion();
        return promotion;
    }

    public void addProductToNewPromotion(@NotNull Long productId, @NotNull @Positive @DecimalMax("99.99") Float discount) {
        coreServiceIntegration.findById(productId).subscribe(productDto -> promotionItemsList.add(productDto, discount));
    }

    public void deletePromotion(@NotNull Long promotionId) {
        promotionRepository.deleteById(promotionId);
    }

    public void setProductDiscountInNewPromotion(@NotNull Long productId, @NotNull @Positive @Digits(integer = 2, fraction = 2) @DecimalMax("99.99") Float discount) {
        promotionItemsList.setProductDiscount(productId, discount);
    }

    public void removeProductFromNewPromotion(@NotNull Long productId) {
        promotionItemsList.remove(productId);
    }

    public void clearNewPromotion() {
        promotionItemsList.clear();
    }

    public Mono<Float> getDiscount(@NotNull Long productId, @NotNull LocalDateTime localDateTimeStart, @NotNull LocalDateTime localDateTimeEnd) {
        ProductDiscount productDiscount = promotionItemsRepository.findProductDiscount(productId, localDateTimeStart, localDateTimeEnd).orElse(() -> 0f);
        return Mono.just(productDiscount.getDiscount());
    }

    public List<Promotion> getActiveByPeriod(@NotNull LocalDateTime start, @NotNull LocalDateTime end) {
        return promotionRepository.findAllByStartedAtIsLessThanAndEndedAtGreaterThan(start, end);
    }

    public List<Promotion> getAllByPeriod(@NotNull LocalDateTime start, @NotNull LocalDateTime end) {
        return promotionRepository.findAllInPeriod(start, end);
    }

    public Mono<CartDto> recalculateCart(@Valid CartDto cartDto, @NotNull LocalDateTime localDateTime) {
        cartDto.getItems().forEach(cartItemDto -> {
            Float discount = getDiscount(cartItemDto.getProductId(), localDateTime, localDateTime).block();

            if (discount > 0f) {
                BigDecimal newItemPricePerProduct = cartItemDto.getPricePerProduct().multiply(BigDecimal.valueOf((100f - discount) / 100));
                BigDecimal newItemPrice = newItemPricePerProduct.multiply(BigDecimal.valueOf(cartItemDto.getQuantity()));
                cartItemDto.setPricePerProduct(newItemPricePerProduct);
                cartItemDto.setPrice(newItemPrice);
            }
        });
        BigDecimal newCartTotalPrice = cartDto.getItems()
                                              .stream()
                                              .map(CartItemDto::getPrice)
                                              .reduce(BigDecimal.ZERO, BigDecimal::add);
        cartDto.setTotalPrice(newCartTotalPrice);
        return Mono.just(cartDto);
    }

}


