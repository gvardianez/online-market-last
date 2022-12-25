package ru.alov.market.promotion.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Entity
@Table(name = "promotions_items")
@NoArgsConstructor
@Data
public class PromotionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @NotNull
    @Column(name = "product_id")
    private Long productId;

    @NotNull
    @Positive
    @DecimalMax("99.99")
    @Column(name = "discount")
    private Float discount;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public PromotionItem(Promotion promotion, Long productId, Float discount) {
        this.promotion = promotion;
        this.productId = productId;
        this.discount = discount;
    }
}
