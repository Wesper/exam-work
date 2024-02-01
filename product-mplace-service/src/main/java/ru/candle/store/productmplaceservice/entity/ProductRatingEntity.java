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

    @Column(name = "sum")
    @NonNull
    private Long sum;

    @Column(name = "product_id")
    @NonNull
    private Long productId;

    @Column(name = "count")
    @NonNull
    private Long count;
}
