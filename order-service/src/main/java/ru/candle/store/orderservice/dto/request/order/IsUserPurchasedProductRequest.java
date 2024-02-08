package ru.candle.store.orderservice.dto.request.order;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IsUserPurchasedProductRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long productId;
}
