package ru.candle.store.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.candle.store.ui.dictionary.Status;

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
