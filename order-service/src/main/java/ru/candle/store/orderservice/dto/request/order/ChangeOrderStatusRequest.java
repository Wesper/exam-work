package ru.candle.store.orderservice.dto.request.order;

import lombok.*;
import org.springframework.lang.NonNull;
import ru.candle.store.orderservice.dictionary.Status;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeOrderStatusRequest {

    @NonNull
    private Long orderId;
    @NonNull
    private Status status;
}
