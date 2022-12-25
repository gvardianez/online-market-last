package ru.alov.market.analytics.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ru.alov.market.api.dto.ProductRatingDto;
import ru.alov.market.api.dto.RequestRatingDto;
import ru.alov.market.api.dto.UserProductsRatingDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(AppTestConfig.class)
public class AnalyticsIntegrationTest {

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private ObjectMapper objectMapper;

  public static LocalDateTime localDateTimeStartFirstTest;
  public static LocalDateTime localDateTimeStartOtherTests;
  public static LocalDateTime localDateTimeEnd;
  public static TypeReference<List<ProductRatingDto>> typeReference;

  @BeforeAll
  public static void init() {
    localDateTimeStartFirstTest = LocalDateTime.of(2022, 12, 15, 1, 0);
    localDateTimeStartOtherTests = LocalDateTime.of(2022, 12, 6, 1, 0);
    localDateTimeEnd = LocalDateTime.of(2022, 12, 16, 1, 0);
    typeReference = new TypeReference<>() {};
  }

  @Test
  public void AnalyticsControllerGetProductQuantityRatingForPeriodTest()
      throws JsonProcessingException {

    RequestRatingDto requestRatingDto =
        new RequestRatingDto(null, localDateTimeStartFirstTest, localDateTimeEnd, null);

    String ratingDtoFlux =
        restTemplate.postForObject(
            "/api/v1/analytical/product-quantity-rating-period", requestRatingDto, String.class);

    List<ProductRatingDto> productRatingDto = objectMapper.readValue(ratingDtoFlux, typeReference);

    Assertions.assertEquals(1, productRatingDto.size());
    Assertions.assertEquals("Молоко", productRatingDto.get(0).getProductDto().getTitle());
    Assertions.assertEquals(2, productRatingDto.get(0).getQuantity());
  }

  @Test
  public void AnalyticsControllerGetProductQuantityAndCostRatingForPeriodTest()
      throws JsonProcessingException {

    RequestRatingDto requestRatingDto =
        new RequestRatingDto(null, localDateTimeStartOtherTests, localDateTimeEnd, null);

    String ratingDtoFlux =
        restTemplate.postForObject(
            "/api/v1/analytical/product-quantity-cost-rating-period",
            requestRatingDto,
            String.class);

    List<ProductRatingDto> productRatingDto = objectMapper.readValue(ratingDtoFlux, typeReference);

    Assertions.assertEquals(3, productRatingDto.size());
    Assertions.assertEquals("Хлеб", productRatingDto.get(1).getProductDto().getTitle());
    Assertions.assertEquals(7, productRatingDto.get(0).getQuantity());
    Assertions.assertEquals(2, productRatingDto.get(2).getQuantity());
    Assertions.assertEquals(BigDecimal.valueOf(280.49), productRatingDto.get(0).getCost());
    Assertions.assertEquals(BigDecimal.valueOf(100.34), productRatingDto.get(1).getCost());
  }

  @Test
  public void AnalyticsControllerGetProductQuantityAndCostRatingForPeriodWithCountTest()
      throws JsonProcessingException {

    RequestRatingDto requestRatingDto =
        new RequestRatingDto(null, localDateTimeStartOtherTests, localDateTimeEnd, 2L);

    String ratingDtoFlux =
        restTemplate.postForObject(
            "/api/v1/analytical/product-quantity-cost-rating-period",
            requestRatingDto,
            String.class);

    List<ProductRatingDto> productRatingDto = objectMapper.readValue(ratingDtoFlux, typeReference);

    Assertions.assertEquals(2, productRatingDto.size());
    Assertions.assertEquals("Сыр", productRatingDto.get(1).getProductDto().getTitle());
    Assertions.assertEquals(7, productRatingDto.get(0).getQuantity());
    Assertions.assertEquals(2, productRatingDto.get(1).getQuantity());
    Assertions.assertEquals(BigDecimal.valueOf(300.24), productRatingDto.get(1).getCost());
  }

  @Test
  public void AnalyticsControllerUserProductsRatingForPeriodTest() {
    RequestRatingDto requestRatingDto =
        new RequestRatingDto("bob", localDateTimeStartOtherTests, localDateTimeEnd, null);

    UserProductsRatingDto userProductsRatingDto =
        restTemplate.postForObject(
            "/api/v1/analytical/user-products-rating-period",
            requestRatingDto,
            UserProductsRatingDto.class);

    Assertions.assertEquals("bob", userProductsRatingDto.getUserProfileDto().getUsername());
    Assertions.assertEquals(3, userProductsRatingDto.getProductRatingDtoList().size());
    Assertions.assertEquals(
        BigDecimal.valueOf(200.35),
        userProductsRatingDto.getProductRatingDtoList().get(0).getCost());
    Assertions.assertEquals(
        1, userProductsRatingDto.getProductRatingDtoList().get(1).getQuantity());
    Assertions.assertEquals(
        "Сыр", userProductsRatingDto.getProductRatingDtoList().get(2).getProductDto().getTitle());
  }
}
