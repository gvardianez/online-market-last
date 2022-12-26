package ru.alov.market.core.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import ru.alov.market.api.dto.ListDto;
import ru.alov.market.api.dto.PageDto;
import ru.alov.market.api.dto.ProductDto;
import ru.alov.market.api.exception.CoreServiceAppError;
import ru.alov.market.api.exception.ResourceNotFoundException;
import ru.alov.market.core.converters.PageConverter;
import ru.alov.market.core.converters.ProductConverter;
import ru.alov.market.core.services.ProductService;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Продукты", description = "Методы работы с продуктами")
public class ProductController {
  private final ProductService productService;
  private final ProductConverter productConverter;
  private final PageConverter pageConverter;

  @Operation(
      summary = "Запрос на получение страницы с продуктами",
      responses = {
        @ApiResponse(
            description = "Успешный ответ",
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = PageDto.class)))
      })
  @GetMapping
  public PageDto<ProductDto> getAllProducts(
      @RequestParam(name = "p", defaultValue = "1")
          @Parameter(description = "Номер страницы", required = true)
          Integer page,
      @RequestParam(name = "page_size", defaultValue = "5")
          @Parameter(description = "Количество товаров на странице", required = true)
          Integer pageSize,
      @RequestParam(name = "title_part", required = false)
          @Parameter(description = "Фильтр по части названия продукта")
          String titlePart,
      @RequestParam(name = "min_price", required = false)
          @Parameter(description = "Фильтр по мин цене продукта")
          Integer minPrice,
      @RequestParam(name = "max_price", required = false)
          @Parameter(description = "Фильтр по макс цене продукта")
          Integer maxPrice) {
    return pageConverter.entityToDto(
        productService
            .findAll(minPrice, maxPrice, titlePart, page, pageSize)
            .map(productConverter::entityToDto));
  }

  @Operation(
      summary = "Запрос на получение продукта по id",
      responses = {
        @ApiResponse(
            description = "Успешный ответ",
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = ProductDto.class))),
        @ApiResponse(
            description = "Продукт не найден",
            responseCode = "404",
            content = @Content(schema = @Schema(implementation = CoreServiceAppError.class)))
      })
  @GetMapping("/{id}")
  public ProductDto getProductById(@PathVariable Long id) {
    return productConverter.entityToDto(
        productService
            .findById(id)
            .orElseThrow(
                () -> new ResourceNotFoundException("Продукт с id: " + id + " не найден")));
  }

  @PostMapping("/get-products")
  public Flux<ProductDto> getProductsByIds(@RequestBody ListDto<Long> longListDto) {
    return productService.findProductsByIds(longListDto).map(productConverter::entityToDto);
  }

  @GetMapping("/get-products-by-period")
  public ListDto<ProductDto> getProductsByCreatedPeriod(
      @RequestParam(name = "start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime start,
      @RequestParam(name = "end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime end) {
    return new ListDto<>(
        productService.findProductsCreatedInPeriod(start, end).stream()
            .map(productConverter::entityToDto)
            .collect(Collectors.toList()));
  }

  @Operation(
      summary = "Запрос на создание нового продукта",
      responses = {@ApiResponse(description = "Продукт успешно создан", responseCode = "201")})
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createNewProduct(@RequestBody ProductDto productDto) {
    productService.createNewProduct(productDto);
  }

  @DeleteMapping("/{id}")
  public void deleteProductById(@PathVariable Long id) {
    productService.deleteById(id);
  }
}
