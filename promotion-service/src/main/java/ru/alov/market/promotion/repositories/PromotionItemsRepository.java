package ru.alov.market.promotion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.alov.market.promotion.entities.PromotionItem;
import ru.alov.market.promotion.repositories.projections.ProductDiscount;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PromotionItemsRepository extends JpaRepository<PromotionItem, Long> {

    @Query(nativeQuery = true, value =
            "SELECT\n" +
                    "SUM(discount) as discount\n" +
                    "FROM promotions_items\n" +
                    "LEFT JOIN promotion ON promotions_items.promotion_id = promotion.\"id\"\n" +
                    "WHERE product_id = :productId AND promotion.start_at < :localDateTimeStart AND promotion.end_at > :localDateTimeEnd\n" +
                    "GROUP BY product_id"
    )
    Optional<ProductDiscount> findProductDiscount(@Param("productId") Long productId, @Param("localDateTimeStart") LocalDateTime localDateTimeStart, @Param("localDateTimeEnd") LocalDateTime localDateTimeEnd);

}
