package ru.candle.store.orderservice.dto.request.basket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteProductRequest {

    @NonNull
    private Long productId;
}
