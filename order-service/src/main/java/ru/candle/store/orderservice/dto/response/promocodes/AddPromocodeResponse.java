package ru.candle.store.orderservice.dto.response.promocodes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddPromocodeResponse {

    private Boolean success;
    private String errorCode;
    private String errorText;
}
