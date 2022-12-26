package ru.alov.market.analytics.configs;

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
import ru.alov.market.analytics.properties.AuthServiceIntegrationProperties;
import ru.alov.market.analytics.properties.CoreServiceIntegrationProperties;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({
  CoreServiceIntegrationProperties.class,
  AuthServiceIntegrationProperties.class
})
public class AppConfig {

  private final CoreServiceIntegrationProperties coreServiceIntegrationProperties;

  private final AuthServiceIntegrationProperties authServiceIntegrationProperties;

  @Bean
  @LoadBalanced
  public WebClient.Builder loadBalancedWebClientBuilder() {
    return WebClient.builder();
  }

  @Bean
  public WebClient coreServiceWebClient() {
    return getWebClient(
        coreServiceIntegrationProperties.getConnectTimeout(),
        coreServiceIntegrationProperties.getReadTimeout(),
        coreServiceIntegrationProperties.getWriteTimeout(),
        coreServiceIntegrationProperties.getUrl());
  }

  @Bean
  public WebClient authServiceWebClient() {
    return getWebClient(
        authServiceIntegrationProperties.getConnectTimeout(),
        authServiceIntegrationProperties.getReadTimeout(),
        authServiceIntegrationProperties.getWriteTimeout(),
        authServiceIntegrationProperties.getUrl());
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
