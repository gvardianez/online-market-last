package ru.alov.market.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.alov.market.core.entities.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  List<Order> findAllByUsername(String username);

  @Query(
      nativeQuery = true,
      value =
          "SELECT *\n"
              + "FROM orders\n"
              + "LEFT JOIN orders_items ON orders.\"id\" = orders_items.order_id\n"
              + "WHERE username = :username AND product_id = :product_id AND status = :status")
  List<Order> findOrdersByProductIdAndUsernameAndOrderStatus(
      @Param("username") String username,
      @Param("product_id") Long productId,
      @Param("status") String status);
}
