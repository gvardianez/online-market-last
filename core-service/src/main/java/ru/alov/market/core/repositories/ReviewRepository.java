package ru.alov.market.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.alov.market.core.entities.Review;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

  @Query(
      value =
          "SELECT review FROM Review review where review.username = ?1 and review.product.id = ?2 ")
  Optional<Review> findByUsernameAndProductId(String username, Long productId);
}
