package ru.alov.market.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "integrations.promotion-service")
@Data
public class PromotionServiceIntegrationProperties {
  private String url;
  private Integer connectTimeout;
  private Integer readTimeout;
  private Integer writeTimeout;
}
