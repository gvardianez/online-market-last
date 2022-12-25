package ru.alov.market.core.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import ru.alov.market.api.dto.ListDto;
import ru.alov.market.api.dto.ProductDto;
import ru.alov.market.api.exception.ResourceNotFoundException;
import ru.alov.market.core.entities.Product;
import ru.alov.market.core.repositories.ProductRepository;
import ru.alov.market.core.repositories.specifications.ProductsSpecifications;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final CategoryService categoryService;

  public Page<Product> findAll(
      Integer minPrice,
      Integer maxPrice,
      String partTitle,
      @NotNull @Min(1) Integer page,
      @NotNull @Min(1) Integer pageSize) {
    Specification<Product> spec = Specification.where(null);
    if (minPrice != null) {
      spec =
          spec.and(ProductsSpecifications.priceGreaterOrEqualsThan(BigDecimal.valueOf(minPrice)));
    }
    if (maxPrice != null) {
      spec =
          spec.and(ProductsSpecifications.priceLessThanOrEqualsThan(BigDecimal.valueOf(maxPrice)));
    }
    if (partTitle != null) {
      spec = spec.and(ProductsSpecifications.titleLike(partTitle));
    }
    return productRepository.findAll(spec, PageRequest.of(page - 1, pageSize));
  }

  public void deleteById(@NotNull Long id) {
    productRepository.deleteById(id);
  }

  public void createNewProduct(@Valid ProductDto productDto) {
    Product product = new Product();
    product.setTitle(productDto.getTitle());
    product.setPrice(productDto.getPrice());
    product.setCategory(
        categoryService
            .findByTitle(productDto.getCategoryTitle())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Категория с названием: "
                            + productDto.getCategoryTitle()
                            + " не найдена")));
    productRepository.save(product);
  }

  public Optional<Product> findById(@NotNull Long id) {
    return productRepository.findById(id);
  }

  public Flux<Product> findProductsByIds(@NotNull ListDto<Long> longListDto) {
    return Flux.fromIterable(productRepository.findAllById(longListDto.getContent()));
  }

  public List<Product> findProductsCreatedInPeriod(
      @NotNull LocalDateTime start, @NotNull LocalDateTime end) {
    return productRepository.findAllByCreatedAtIsBetween(start, end);
  }
}
