package ru.alov.market.cart.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "integrations.product-service")
@Data
public class CoreServiceIntegrationProperties {
  private String url;
  private Integer connectTimeout;
  private Integer readTimeout;
  private Integer writeTimeout;
}
