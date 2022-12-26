package ru.alov.market.analytics.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "analytical_data")
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
public class Analytical {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @NotNull
  @Column(name = "product_id")
  private Long productId;

  @NotNull
  @Column(name = "order_id")
  private Long orderId;

  @NotBlank
  @Column(name = "username")
  private String username;

  @NotNull
  @Min(1)
  @Column(name = "quantity")
  private Integer quantity;

  @NotNull
  @Positive
  @Column(name = "price_per_product")
  private BigDecimal pricePerProduct;

  @NotNull
  @Column(name = "bought_at")
  private LocalDateTime boughtAt;

  @Column(name = "created_at")
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public Analytical(
      Long productId,
      Long orderId,
      String username,
      Integer quantity,
      BigDecimal pricePerProduct,
      LocalDateTime boughtAt) {
    this.productId = productId;
    this.orderId = orderId;
    this.username = username;
    this.quantity = quantity;
    this.pricePerProduct = pricePerProduct;
    this.boughtAt = boughtAt;
  }
}
