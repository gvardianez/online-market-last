package ru.alov.market.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class NeedAdminRoleFilter extends AbstractGatewayFilterFactory<NeedAdminRoleFilter.Config> {

  public NeedAdminRoleFilter() {
    super(Config.class);
  }

  @Override
  public GatewayFilter apply(NeedAdminRoleFilter.Config config) {
    return (exchange, chain) -> {
      ServerHttpRequest request = exchange.getRequest();
      if (!request.getHeaders().containsKey("role")
          || !Objects.requireNonNull(request.getHeaders().get("role")).contains("ROLE_ADMIN")) {
        return this.onError(exchange, "Insufficient access rights", HttpStatus.UNAUTHORIZED);
      }
      return chain.filter(exchange);
    };
  }

  public static class Config {}

  private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(httpStatus);
    return response.setComplete();
  }
}
