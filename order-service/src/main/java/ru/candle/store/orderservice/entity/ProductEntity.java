package ru.candle.store.orderservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity {

    @Id
    private Long productId;

    private String imageId;

    private String title;

    private Long price;

    private Long promoPrice;

    private Long count;
}
