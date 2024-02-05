package ru.candle.store.orderservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "promocodes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class PromocodeEntity {

    @Id
    @Column(name = "promocode")
    private String promocode;

    @Column(name = "percent")
    private Long percent;

    @Column(name = "actual")
    private Boolean actual;

}
