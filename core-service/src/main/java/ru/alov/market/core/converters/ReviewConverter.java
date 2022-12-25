package ru.alov.market.core.converters;

import org.springframework.stereotype.Component;
import ru.alov.market.api.dto.ReviewDto;
import ru.alov.market.core.entities.Review;

@Component
public class ReviewConverter {

  public ReviewDto entityToDto(Review review) {
    return new ReviewDto(
        review.getId(),
        review.getProduct().getId(),
        review.getUsername(),
        review.getGrade(),
        review.getDescription(),
        review.getCreatedAt());
  }
}
