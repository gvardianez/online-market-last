package ru.alov.market.core.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @NotBlank
  @Column(name = "username")
  private String username;

  @NotNull
  @Email
  @Column(name = "email")
  private String email;

  @NotBlank
  @Column(name = "address")
  private String address;

  @NotBlank
  @Column(name = "phone")
  private String phone;

  @NotNull
  @Positive
  @Column(name = "total_price")
  private BigDecimal totalPrice;

  @NotBlank
  @Column(name = "status")
  private String status;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  private List<OrderItem> items;

  @Column(name = "created_at")
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public enum OrderStatus {
    CREATED,
    PAID,
    CANCELED,
    COMPLETED
  }
}
