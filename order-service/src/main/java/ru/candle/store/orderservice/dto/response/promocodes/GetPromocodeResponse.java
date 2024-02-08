package ru.candle.store.orderservice.dto.response.promocodes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetPromocodeResponse {

    private Boolean success;
    private String promocode;
    private Long percent;
    private String errorCode;
    private String errorText;
}
