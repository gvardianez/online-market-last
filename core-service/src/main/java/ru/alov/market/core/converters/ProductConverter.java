package ru.alov.market.core.converters;

import org.springframework.stereotype.Component;
import ru.alov.market.api.dto.ProductDto;
import ru.alov.market.core.entities.Product;

@Component
public class ProductConverter {

  public ProductDto entityToDto(Product p) {
    return new ProductDto(p.getId(), p.getTitle(), p.getPrice(), p.getCategory().getTitle());
  }
}
