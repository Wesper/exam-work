package ru.candle.store.productmplaceservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "ratings")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class ProductRatingEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rating")
    @NonNull
    private Long rating;

    @Column(name = "product_id")
    @NonNull
    private Long productId;

    @Column(name = "user_id")
    @NonNull
    private Long userId;

    public ProductRatingEntity(@NonNull Long id, @NonNull Long rating, @NonNull Long productId, @NonNull Long userId) {
        this.id = id;
        this.rating = rating;
        this.productId = productId;
        this.userId = userId;
    }
}
