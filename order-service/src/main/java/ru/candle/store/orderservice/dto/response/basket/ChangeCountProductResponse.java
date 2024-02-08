package ru.candle.store.orderservice.dto.response.basket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeCountProductResponse {

    private Boolean success;
    private String errorCode;
    private String errorText;
}
