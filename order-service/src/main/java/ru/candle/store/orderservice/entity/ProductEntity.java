package ru.candle.store.orderservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@DynamicUpdate
public class ProductEntity {

    @Id
    @NonNull
    private Long productId;

    @NonNull
    private String imageId;

    @NonNull
    private String title;

    @NonNull
    private Long price;

    @NonNull
    private Long promoPrice;

    @NonNull
    private Long count;
}
