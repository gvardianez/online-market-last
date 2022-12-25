package ru.alov.market.core.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
public class Review {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  @NotBlank
  @Column(name = "username")
  private String username;

  @NotNull
  @Min(1)
  @Max(5)
  @Column(name = "grade")
  private Integer grade;

  @NotBlank
  @Column(name = "description")
  private String description;

  @Column(name = "created_at")
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public Review(Product product, String username, Integer grade, String description) {
    this.product = product;
    this.username = username;
    this.grade = grade;
    this.description = description;
  }
}
