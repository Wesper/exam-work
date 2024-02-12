package ru.candle.store.orderservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.DynamicUpdate;
import ru.candle.store.orderservice.dictionary.Status;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class OrderEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Long totalPromoPrice;

    @Column(name = "details")
    @NonNull
    private String details;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @NonNull
    private Status status;
}
