package ru.alov.market.promotion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.alov.market.promotion.entities.Promotion;

import java.time.LocalDateTime;
import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    List<Promotion> findAllByStartedAtIsLessThanAndEndedAtGreaterThan(LocalDateTime start, LocalDateTime end);

    @Query(nativeQuery = true, value =
            "SELECT *\n" +
                    "FROM promotion\n" +
                    "WHERE (promotion.start_at > :start OR :start BETWEEN promotion.start_at and promotion.end_at) AND (:end > promotion.end_at or :end BETWEEN promotion.start_at and promotion.end_at)"
    )
    List<Promotion> findAllInPeriod(LocalDateTime start, LocalDateTime end);

}
