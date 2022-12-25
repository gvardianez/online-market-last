package ru.alov.market.core.configs;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;
import ru.alov.market.core.properties.CartServiceIntegrationProperties;
import ru.alov.market.core.properties.PromotionServiceIntegrationProperties;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({
  CartServiceIntegrationProperties.class,
  PromotionServiceIntegrationProperties.class
})
public class AppConfig {

  private final CartServiceIntegrationProperties cartServiceIntegrationProperties;
  private final PromotionServiceIntegrationProperties promotionServiceIntegrationProperties;

  @Bean
  @LoadBalanced
  public WebClient.Builder loadBalancedWebClientBuilder() {
    return WebClient.builder();
  }

  @Bean
  public WebClient cartServiceWebClient() {
    return getWebClient(
        cartServiceIntegrationProperties.getConnectTimeout(),
        cartServiceIntegrationProperties.getReadTimeout(),
        cartServiceIntegrationProperties.getWriteTimeout(),
        cartServiceIntegrationProperties.getUrl());
  }

  @Bean
  public WebClient promotionServiceWebClient() {
    return getWebClient(
        promotionServiceIntegrationProperties.getConnectTimeout(),
        promotionServiceIntegrationProperties.getReadTimeout(),
        promotionServiceIntegrationProperties.getWriteTimeout(),
        promotionServiceIntegrationProperties.getUrl());
  }

  private WebClient getWebClient(
      Integer connectTimeout, Integer readTimeout, Integer writeTimeout, String url) {
    TcpClient tcpClient =
        TcpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
            .doOnConnected(
                connection -> {
                  connection.addHandlerLast(
                      new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS));
                  connection.addHandlerLast(
                      new WriteTimeoutHandler(writeTimeout, TimeUnit.MILLISECONDS));
                });

    return loadBalancedWebClientBuilder()
        .baseUrl(url)
        .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
        .build();
  }
}
