package ru.candle.store.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.candle.store.orderservice.dictionary.Status;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    private Long orderId;
    private String date;
    private Long totalPrice;
    private Long totalPromoPrice;
    private Status status;
}
