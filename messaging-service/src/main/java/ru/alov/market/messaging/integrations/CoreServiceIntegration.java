package ru.alov.market.messaging.integrations;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.alov.market.api.dto.ListDto;
import ru.alov.market.api.dto.ProductDto;

import java.time.LocalDateTime;

@FeignClient(name = "core-service")
public interface CoreServiceIntegration {

  @GetMapping("/market-core/api/v1/products/get-products-by-period")
  ListDto<ProductDto> getProductsByCreatedPeriod(
      @RequestParam(name = "start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime start,
      @RequestParam(name = "end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime end);
}
