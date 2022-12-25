package ru.alov.market.core.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders_items")
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
public class OrderItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  @NotNull
  @Positive
  @Column(name = "price_per_product")
  private BigDecimal pricePerProduct;

  @NotNull
  @Positive
  @Column(name = "price")
  private BigDecimal price;

  @NotNull
  @Min(1)
  @Column(name = "quantity")
  private int quantity;

  @Column(name = "created_at")
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public OrderItem(
      @NotNull Order order,
      @NotNull Product product,
      @NotNull @Positive BigDecimal pricePerProduct,
      @NotNull @Positive BigDecimal price,
      @NotNull @Min(1) int quantity) {
    this.order = order;
    this.product = product;
    this.pricePerProduct = pricePerProduct;
    this.price = price;
    this.quantity = quantity;
  }
}
