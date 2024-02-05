package ru.candle.store.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import ru.candle.store.orderservice.dictionary.Status;
import ru.candle.store.orderservice.dto.Product;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class OrderEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    @NonNull
    private Long userId;

    @Column(name = "date")
    @NonNull
    private String date;

    @Column(name = "address")
    @NonNull
    private String address;

    @Column(name = "promocode")
    private String promocode;

    @Column(name = "total_price")
    @NonNull
    private Long totalPrice;

    @Column(name = "total_promo_price")
    private Long total_promo_price;

    @Column(name = "details")
    @OneToMany
    @NonNull
    private List<ProductEntity> details;

    @Column(name = "status")
    @NonNull
    private Status status;
}
