package ru.alov.market.messaging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableScheduling
@PropertySource({"classpath:mail.properties", "classpath:telegram.properties"})
public class MessagingServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(MessagingServiceApplication.class, args);
  }
}
