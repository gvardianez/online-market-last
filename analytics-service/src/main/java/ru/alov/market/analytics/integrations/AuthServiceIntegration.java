package ru.alov.market.analytics.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.alov.market.api.dto.UserProfileDto;
import ru.alov.market.api.exception.AuthServiceAppError;
import ru.alov.market.api.exception.AuthServiceIntegrationException;

@Component
@RequiredArgsConstructor
public class AuthServiceIntegration {

  private final WebClient authServiceWebClient;

  public Mono<UserProfileDto> getUserProfileDto(String username) {
    return authServiceWebClient
        .get()
        .uri("/api/v1/account")
        .header("username", username)
        .retrieve()
        .onStatus(
            HttpStatus::is4xxClientError,
            clientResponse ->
                clientResponse
                    .bodyToMono(AuthServiceAppError.class)
                    .map(
                        body ->
                            new AuthServiceIntegrationException(
                                AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_BAD_REQUEST
                                        .name()
                                    + ": "
                                    + body.getMessage())))
        .onStatus(
            HttpStatus::is5xxServerError,
            clientResponse ->
                Mono.error(
                    new AuthServiceIntegrationException(
                        AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_INTERNAL_EXCEPTION
                            .name())))
        .bodyToMono(UserProfileDto.class);
  }
}
