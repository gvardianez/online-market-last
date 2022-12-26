package ru.alov.market.core.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.alov.market.api.dto.CartDto;
import ru.alov.market.api.dto.NumberDto;
import ru.alov.market.api.dto.RecalculateCartRequestDto;
import ru.alov.market.api.enums.RoleStatus;
import ru.alov.market.api.exception.PromotionServiceAppError;
import ru.alov.market.api.exception.PromotionServiceIntegrationException;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PromotionServiceIntegration {

  private final WebClient promotionServiceWebClient;

  public Mono<NumberDto> getProductDiscount(
      Long productId, LocalDateTime start, LocalDateTime end) {
    return promotionServiceWebClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/api/v1/promotions/discount/" + productId)
                    .queryParam("start", start)
                    .queryParam("end", end)
                    .build())
        .header("role", RoleStatus.ROLE_ADMIN.toString())
        .retrieve()
        .onStatus(
            HttpStatus::is4xxClientError,
            clientResponse ->
                clientResponse
                    .bodyToMono(PromotionServiceAppError.class)
                    .map(
                        body ->
                            new PromotionServiceIntegrationException(
                                PromotionServiceAppError.PromotionServiceErrors
                                        .PROMOTION_SERVICE_BAD_REQUEST
                                        .name()
                                    + ": "
                                    + body.getMessage())))
        .onStatus(
            HttpStatus::is5xxServerError,
            clientResponse ->
                Mono.error(
                    new PromotionServiceIntegrationException(
                        PromotionServiceAppError.PromotionServiceErrors
                            .PROMOTION_SERVICE_INTERNAL_EXCEPTION
                            .name())))
        .bodyToMono(NumberDto.class);
  }

  public Mono<CartDto> getRecalculateCart(RecalculateCartRequestDto recalculateCartRequestDto) {
    return promotionServiceWebClient
        .post()
        .uri("/api/v1/promotions/recalculate-cart/")
        .header("role", RoleStatus.ROLE_ADMIN.toString())
        .bodyValue(recalculateCartRequestDto)
        .retrieve()
        .onStatus(
            HttpStatus::is4xxClientError,
            clientResponse ->
                clientResponse
                    .bodyToMono(PromotionServiceAppError.class)
                    .map(
                        body ->
                            new PromotionServiceIntegrationException(
                                PromotionServiceAppError.PromotionServiceErrors
                                        .PROMOTION_SERVICE_BAD_REQUEST
                                        .name()
                                    + ": "
                                    + body.getMessage())))
        .onStatus(
            HttpStatus::is5xxServerError,
            clientResponse ->
                Mono.error(
                    new PromotionServiceIntegrationException(
                        PromotionServiceAppError.PromotionServiceErrors
                            .PROMOTION_SERVICE_INTERNAL_EXCEPTION
                            .name())))
        .bodyToMono(CartDto.class);
  }
}
