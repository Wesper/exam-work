package ru.candle.store.orderservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "baskets")
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@DynamicUpdate
public class BasketEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    @NonNull
    private Long userId;

    @Column(name = "product_id")
    @NonNull
    private Long productId;

    @Column(name = "count")
    @NonNull
    private Long count;
}
