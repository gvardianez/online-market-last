package ru.alov.market.analytics.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.alov.market.analytics.entities.Analytical;
import ru.alov.market.analytics.repositories.projections.AnalyticalProductsQuantityAndCostRating;
import ru.alov.market.analytics.repositories.projections.AnalyticalProductsQuantityRating;
import ru.alov.market.analytics.repositories.projections.AnalyticalUserProductsStatistic;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnalyticalRepository extends JpaRepository<Analytical, Long> {
  @Query(
      nativeQuery = true,
      value =
          "SELECT \n"
              + "product_id as productId,\n"
              + "SUM(quantity) as quantity\n"
              + "FROM analytical_data\n"
              + "WHERE bought_at BETWEEN :localDateTimeStart AND :localDateTimeEnd\n"
              + "GROUP BY product_id ")
  List<AnalyticalProductsQuantityRating> findProductQuantityRating(
      @Param("localDateTimeStart") LocalDateTime localDateTimeStart,
      @Param("localDateTimeEnd") LocalDateTime localDateTimeEnd);

  @Query(
      nativeQuery = true,
      value =
          "SELECT \n"
              + "product_id as productId,\n"
              + "SUM(quantity) as quantity\n"
              + "FROM analytical_data\n"
              + "WHERE bought_at BETWEEN :localDateTimeStart AND :localDateTimeEnd\n"
              + "GROUP BY product_id \n"
              + "ORDER BY quantity DESC\n"
              + "LIMIT :count")
  List<AnalyticalProductsQuantityRating> findProductQuantityRatingWithCount(
      @Param("localDateTimeStart") LocalDateTime localDateTimeStart,
      @Param("localDateTimeEnd") LocalDateTime localDateTimeEnd,
      @Param("count") Long count);

  @Query(
      nativeQuery = true,
      value =
          "SELECT \n"
              + "product_id as productId,\n"
              + "SUM(quantity) as quantity,\n"
              + "SUM(quantity*price_per_product) as cost\n"
              + "FROM analytical_data\n"
              + "WHERE bought_at BETWEEN :localDateTimeStart AND :localDateTimeEnd\n"
              + "GROUP BY product_id ")
  List<AnalyticalProductsQuantityAndCostRating> findProductQuantityAndCostRating(
      @Param("localDateTimeStart") LocalDateTime localDateTimeStart,
      @Param("localDateTimeEnd") LocalDateTime localDateTimeEnd);

  @Query(
      nativeQuery = true,
      value =
          "SELECT \n"
              + "product_id as productId,\n"
              + "SUM(quantity) as quantity,\n"
              + "SUM(quantity*price_per_product) as cost\n"
              + "FROM analytical_data\n"
              + "WHERE bought_at BETWEEN :localDateTimeStart AND :localDateTimeEnd\n"
              + "GROUP BY product_id \n"
              + "ORDER BY quantity DESC\n"
              + "LIMIT :count")
  List<AnalyticalProductsQuantityAndCostRating> findProductQuantityAndCostRatingWithCount(
      @Param("localDateTimeStart") LocalDateTime localDateTimeStart,
      @Param("localDateTimeEnd") LocalDateTime localDateTimeEnd,
      @Param("count") Long count);

  @Query(
      nativeQuery = true,
      value =
          "SELECT \n"
              + "username,\n"
              + "product_id as productId,\n"
              + "SUM(quantity) as quantity,\n"
              + "SUM(quantity*price_per_product) as cost\n"
              + "FROM analytical_data\n"
              + "WHERE username = :username and bought_at BETWEEN :localDateTimeStart AND :localDateTimeEnd\n"
              + "GROUP BY product_id ,username")
  List<AnalyticalUserProductsStatistic> findUserProductStatistic(
      @Param("username") String username,
      @Param("localDateTimeStart") LocalDateTime localDateTimeStart,
      @Param("localDateTimeEnd") LocalDateTime localDateTimeEnd);
}
