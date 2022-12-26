package ru.alov.market.analytics.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.alov.market.analytics.integrations.AuthServiceIntegration;
import ru.alov.market.analytics.integrations.CoreServiceIntegration;
import ru.alov.market.analytics.repositories.AnalyticalRepository;
import ru.alov.market.analytics.repositories.projections.AnalyticalProductsQuantityAndCostRating;
import ru.alov.market.analytics.repositories.projections.AnalyticalProductsQuantityRating;
import ru.alov.market.analytics.repositories.projections.AnalyticalUserProductsStatistic;
import ru.alov.market.api.dto.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor
public class AnalyticalService {

  private final AnalyticalRepository analyticalRepository;
  private final CoreServiceIntegration coreServiceIntegration;
  private final AuthServiceIntegration authServiceIntegration;
  private final RedisTemplate<String, Object> redisTemplate;

  public Flux<ProductRatingDto> getProductQuantityRatingYesterday(
      LocalDateTime localDateTimeStart, LocalDateTime localDateTimeEnd) {
    List<ProductRatingDto> productQuantityRating =
        (List<ProductRatingDto>)
            redisTemplate.opsForValue().get(LocalDate.from(localDateTimeStart).toString());
    if (productQuantityRating == null) {
      List<AnalyticalProductsQuantityRating> analyticalProductsQuantityRatings =
          analyticalRepository.findProductQuantityRating(localDateTimeStart, localDateTimeEnd);
      List<Long> productIds =
          analyticalProductsQuantityRatings.stream()
              .map(AnalyticalProductsQuantityRating::getProductId)
              .collect(Collectors.toList());
      Mono<List<ProductRatingDto>> listMono =
          getProductQuantityRating(analyticalProductsQuantityRatings, productIds).collectList();
      return listMono
          .doOnSuccess(
              productQuantityRatingDtos ->
                  redisTemplate
                      .opsForValue()
                      .set(
                          LocalDate.from(localDateTimeStart).toString(), productQuantityRatingDtos))
          .flatMapIterable(productRatingDtoList -> productRatingDtoList);
    } else return Flux.fromIterable(productQuantityRating);
  }

  public Flux<ProductRatingDto> getProductQuantityRatingPeriod(
      @Valid RequestRatingDto requestRatingDto) {
    if (requestRatingDto.getCount() != null) {
      return getProductQuantityRatingPeriod(
          requestRatingDto.getLocalDateTimeStart(),
          requestRatingDto.getLocalDateTimeEnd(),
          requestRatingDto.getCount());
    } else
      return getProductQuantityRatingPeriod(
          requestRatingDto.getLocalDateTimeStart(), requestRatingDto.getLocalDateTimeEnd());
  }

  private Flux<ProductRatingDto> getProductQuantityRatingPeriod(
      LocalDateTime localDateTimeStart, LocalDateTime localDateTimeEnd) {
    List<AnalyticalProductsQuantityRating> analyticalProductsQuantityRatings =
        analyticalRepository.findProductQuantityRating(localDateTimeStart, localDateTimeEnd);
    List<Long> productIds = getProductIds(analyticalProductsQuantityRatings);
    return getProductQuantityRating(analyticalProductsQuantityRatings, productIds);
  }

  private Flux<ProductRatingDto> getProductQuantityRatingPeriod(
      LocalDateTime localDateTimeStart, LocalDateTime localDateTimeEnd, Long count) {
    List<AnalyticalProductsQuantityRating> analyticalProductsQuantityRatings =
        analyticalRepository.findProductQuantityRatingWithCount(
            localDateTimeStart, localDateTimeEnd, count);
    List<Long> productIds = getProductIds(analyticalProductsQuantityRatings);
    return getProductQuantityRating(analyticalProductsQuantityRatings, productIds);
  }

  private List<Long> getProductIds(
      List<AnalyticalProductsQuantityRating> analyticalProductsQuantityRatings) {
    return analyticalProductsQuantityRatings.stream()
        .map(AnalyticalProductsQuantityRating::getProductId)
        .collect(Collectors.toList());
  }

  private Flux<ProductRatingDto> getProductQuantityRating(
      List<AnalyticalProductsQuantityRating> analyticalProductsQuantityRatings,
      List<Long> productIds) {
    return coreServiceIntegration
        .getListProductDto(new ListDto<>(productIds))
        .collectList()
        .map(
            productDtos -> {
              List<ProductRatingDto> productRatingDtoList = new ArrayList<>();
              for (AnalyticalProductsQuantityRating analyticalProductsQuantityRating :
                  analyticalProductsQuantityRatings)
                for (ProductDto productDto : productDtos) {
                  {
                    if (productDto
                        .getId()
                        .equals(analyticalProductsQuantityRating.getProductId())) {
                      productRatingDtoList.add(
                          new ProductRatingDto(
                              productDto, analyticalProductsQuantityRating.getQuantity()));
                      break;
                    }
                  }
                }
              return productRatingDtoList;
            })
        .flatMapIterable(productRatingDtoList -> productRatingDtoList);
  }

  public Flux<ProductRatingDto> getProductQuantityAndCostRating(
      @Valid RequestRatingDto requestRatingDto) {
    if (requestRatingDto.getCount() != null) {
      return getProductQuantityAndCostRating(
          requestRatingDto.getLocalDateTimeStart(),
          requestRatingDto.getLocalDateTimeEnd(),
          requestRatingDto.getCount());
    } else
      return getProductQuantityAndCostRating(
          requestRatingDto.getLocalDateTimeStart(), requestRatingDto.getLocalDateTimeEnd());
  }

  private Flux<ProductRatingDto> getProductQuantityAndCostRating(
      LocalDateTime localDateTimeStart, LocalDateTime localDateTimeEnd) {
    List<AnalyticalProductsQuantityAndCostRating> analyticalProductsQuantityAndCostRatings =
        analyticalRepository.findProductQuantityAndCostRating(localDateTimeStart, localDateTimeEnd);
    return getFluxProductRatingDto(analyticalProductsQuantityAndCostRatings);
  }

  private Flux<ProductRatingDto> getProductQuantityAndCostRating(
      LocalDateTime localDateTimeStart, LocalDateTime localDateTimeEnd, Long count) {
    List<AnalyticalProductsQuantityAndCostRating> analyticalProductsQuantityAndCostRatings =
        analyticalRepository.findProductQuantityAndCostRatingWithCount(
            localDateTimeStart, localDateTimeEnd, count);
    return getFluxProductRatingDto(analyticalProductsQuantityAndCostRatings);
  }

  private Flux<ProductRatingDto> getFluxProductRatingDto(
      List<AnalyticalProductsQuantityAndCostRating> analyticalProductsQuantityAndCostRatings) {
    List<Long> productIds =
        analyticalProductsQuantityAndCostRatings.stream()
            .map(AnalyticalProductsQuantityAndCostRating::getProductId)
            .collect(Collectors.toList());
    return coreServiceIntegration
        .getListProductDto(new ListDto<>(productIds))
        .collectList()
        .map(
            productDtos -> {
              List<ProductRatingDto> productRatingDtoList = new ArrayList<>();
              for (AnalyticalProductsQuantityAndCostRating analyticalProductsQuantityAndCostRating :
                  analyticalProductsQuantityAndCostRatings)
                for (ProductDto productDto : productDtos) {
                  {
                    if (productDto
                        .getId()
                        .equals(analyticalProductsQuantityAndCostRating.getProductId())) {
                      productRatingDtoList.add(
                          new ProductRatingDto(
                              productDto,
                              analyticalProductsQuantityAndCostRating.getQuantity(),
                              analyticalProductsQuantityAndCostRating.getCost()));
                      break;
                    }
                  }
                }
              return productRatingDtoList;
            })
        .flatMapIterable(productRatingDtoList -> productRatingDtoList);
  }

  public Mono<UserProductsRatingDto> getUserProductsStatistic(
      @Valid RequestRatingDto requestRatingDto) {
    List<AnalyticalUserProductsStatistic> analyticalUserProductsStatistics =
        analyticalRepository.findUserProductStatistic(
            requestRatingDto.getUsername(),
            requestRatingDto.getLocalDateTimeStart(),
            requestRatingDto.getLocalDateTimeEnd());
    List<Long> productIds =
        analyticalUserProductsStatistics.stream()
            .map(AnalyticalUserProductsStatistic::getProductId)
            .collect(Collectors.toList());
    return authServiceIntegration
        .getUserProfileDto(requestRatingDto.getUsername())
        .zipWith(
            coreServiceIntegration.getListProductDto(new ListDto<>(productIds)).collectList(),
            (userProfileDto, productDtos) -> {
              List<ProductRatingDto> productRatingDtoList = new ArrayList<>();
              for (ProductDto productDto : productDtos) {
                for (AnalyticalUserProductsStatistic analyticalUserProductsStatistic :
                    analyticalUserProductsStatistics) {
                  if (productDto.getId().equals(analyticalUserProductsStatistic.getProductId())) {
                    productRatingDtoList.add(
                        new ProductRatingDto(
                            productDto,
                            analyticalUserProductsStatistic.getQuantity(),
                            analyticalUserProductsStatistic.getCost()));
                    break;
                  }
                }
              }
              return new UserProductsRatingDto(userProfileDto, productRatingDtoList);
            });
  }
}
