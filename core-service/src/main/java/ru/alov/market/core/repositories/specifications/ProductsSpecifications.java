package ru.alov.market.core.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.alov.market.core.entities.Product;

import java.math.BigDecimal;

public class ProductsSpecifications {

  public static Specification<Product> priceGreaterOrEqualsThan(BigDecimal price) {
    return (root, criteriaQuery, criteriaBuilder) ->
        criteriaBuilder.greaterThanOrEqualTo(root.get("price"), price);
  }

  public static Specification<Product> priceLessThanOrEqualsThan(BigDecimal price) {
    return (root, criteriaQuery, criteriaBuilder) ->
        criteriaBuilder.lessThanOrEqualTo(root.get("price"), price);
  }

  public static Specification<Product> titleLike(String titlePart) {
    return (root, criteriaQuery, criteriaBuilder) ->
        criteriaBuilder.like(root.get("title"), String.format("%%%s%%", titlePart));
  }
}
