package ru.candle.store.orderservice.dto.response.basket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddProductResponse {

    private Boolean success;
    private String errorCode;
    private String errorText;
}
