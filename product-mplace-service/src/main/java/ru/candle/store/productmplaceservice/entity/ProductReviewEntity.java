package ru.candle.store.productmplaceservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "reviews")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class ProductReviewEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    @NonNull
    private Long userId;

    @Column(name = "product_id")
    @NonNull
    private Long productId;

    @Column(name = "review")
    @NonNull
    private String review;
}
