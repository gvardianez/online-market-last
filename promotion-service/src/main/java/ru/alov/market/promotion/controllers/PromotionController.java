package ru.alov.market.promotion.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.alov.market.api.dto.*;
import ru.alov.market.api.enums.KafkaTopic;
import ru.alov.market.promotion.converters.mapstruct.PromotionMapper;
import ru.alov.market.promotion.services.KafkaProducerService;
import ru.alov.market.promotion.services.PromotionService;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/promotions")
@RequiredArgsConstructor
@Tag(name = "Акции", description = "Методы работы с акциями на товары")
public class PromotionController {

    private final PromotionService promotionService;
    private final PromotionMapper promotionMapper;
    private final KafkaProducerService kafkaProducerService;

    @Operation(
            summary = "Запрос на получение активных акций по определенному периоду",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ListDto.class))
                    )
            }
    )
    @GetMapping("/active")
    public ListDto<PromotionDto> getActivePromotionsByPeriod(@RequestParam(name = "start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start, @RequestParam(name = "end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return new ListDto<>(promotionService.getActiveByPeriod(start, end)
                                             .stream()
                                             .map(promotionMapper::entityToDto)
                                             .collect(Collectors.toList()));
    }

    @Operation(
            summary = "Запрос на получение всех акций по определенному периоду",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ListDto.class))
                    )
            }
    )
    @GetMapping
    public ListDto<PromotionDto> getAllPromotionsByPeriod(@RequestParam(name = "start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start, @RequestParam(name = "end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return new ListDto<>(promotionService.getAllByPeriod(start, end)
                                             .stream()
                                             .map(promotionMapper::entityToDto)
                                             .collect(Collectors.toList()));
    }

    @Operation(
            summary = "Запрос на создание новой акции",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = PromotionDto.class))
                    )
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PromotionDto createPromotion(@RequestBody PromotionDetailsDto promotionDetailsDto) {
        PromotionDto promotionDto = promotionMapper.entityToDto(promotionService.createNewPromotion(promotionDetailsDto));
        kafkaProducerService.sendPromotionDto(KafkaTopic.PROMOTION_DTO.toString(), promotionDto);
        return promotionDto;
    }

    @Operation(
            summary = "Запрос на добавление товара в новую акцию",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    )
            }
    )
    @GetMapping("/add-product")
    public void addProductToNewPromotion(@RequestParam Long productId, @RequestParam Float discount) {
        promotionService.addProductToNewPromotion(productId, discount);
    }

    @Operation(
            summary = "Запрос на удаление товара из новой акции",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    )
            }
    )
    @GetMapping("/remove/{productId}")
    public void removeProductFromNewPromotion(@PathVariable Long productId) {
        promotionService.removeProductFromNewPromotion(productId);
    }

    @Operation(
            summary = "Запрос на удаление всех позиций из создаваемой акции",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    )
            }
    )
    @GetMapping("/clear")
    public void clearNewPromotion() {
        promotionService.clearNewPromotion();
    }

    @Operation(
            summary = "Запрос на установление скидки на опреленную позицию по товару в новой акции",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Не найден продукт для назначения скидки", responseCode = "404"
                    )
            }
    )
    @GetMapping("/set-discount")
    public void setDiscountInNewPromotion(@RequestParam Long productId, @RequestParam Float discount) {
        promotionService.setProductDiscountInNewPromotion(productId, discount);
    }

    @Operation(
            summary = "Запрос на получение скидки на определенную позицию по товару в существующих активных акциях за определенный период",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    )
            }
    )
    @GetMapping("/discount/{productId}")
    public Mono<NumberDto<Float>> getProductDiscount(@PathVariable Long productId,
                                                     @RequestParam(name = "start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                                     @RequestParam(name = "end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return promotionService.getDiscount(productId, start, end).map(NumberDto::new);
    }

    @Operation(
            summary = "Запрос на пересчет стоимости корзины, учитывая активные акции на определенный период",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CartDto.class))
                    )
            }
    )
    @PostMapping("/recalculate-cart/")
    public Mono<CartDto> recalculateCart(@RequestBody RecalculateCartRequestDto recalculateCartRequestDto) {
        return promotionService.recalculateCart(recalculateCartRequestDto.getCartDto(), recalculateCartRequestDto.getLocalDateTime());
    }


    @Operation(
            summary = "Запрос на удаление акции по Id",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    )
            }
    )
    @DeleteMapping("/{promotionId}")
    public void delete(@PathVariable Long promotionId) {
        promotionService.deletePromotion(promotionId);
    }

}
