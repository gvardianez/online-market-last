package ru.alov.market.messaging.integrations;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.alov.market.api.dto.ListDto;

@FeignClient(name = "auth-service")
public interface AuthServiceIntegration {

  @GetMapping("/market-auth/api/v1/account/subscribers")
  ListDto<String> getSubscribersEmails(@RequestHeader String role);
}
