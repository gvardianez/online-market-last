package ru.alov.market.analytics.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.alov.market.api.dto.ListDto;
import ru.alov.market.api.dto.ProductDto;
import ru.alov.market.api.exception.CoreServiceAppError;
import ru.alov.market.api.exception.CoreServiceIntegrationException;

@Component
@RequiredArgsConstructor
public class CoreServiceIntegration {

  private final WebClient coreServiceWebClient;

  public Flux<ProductDto> getListProductDto(ListDto<Long> productIdList) {
    return coreServiceWebClient
        .post()
        .uri("/api/v1/products/get-products")
        .bodyValue(productIdList)
        .retrieve()
        .onStatus(
            HttpStatus::is4xxClientError,
            clientResponse ->
                clientResponse
                    .bodyToMono(CoreServiceAppError.class)
                    .map(
                        body ->
                            new CoreServiceIntegrationException(
                                CoreServiceAppError.CoreServiceErrors.CORE_SERVICE_BAD_REQUEST
                                        .name()
                                    + ": "
                                    + body.getMessage())))
        .onStatus(
            HttpStatus::is5xxServerError,
            clientResponse ->
                Mono.error(
                    new CoreServiceIntegrationException(
                        CoreServiceAppError.CoreServiceErrors.CORE_SERVICE_INTERNAL_EXCEPTION
                            .name())))
        .bodyToFlux(ProductDto.class);
  }
}
