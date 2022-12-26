package ru.alov.market.core.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.alov.market.core.entities.Category;
import ru.alov.market.core.repositories.CategoryRepository;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class CategoryService {
  private final CategoryRepository categoryRepository;

  public Optional<Category> findByTitle(@NotBlank String title) {
    return categoryRepository.findByTitle(title);
  }
}
