package ru.candle.store.orderservice.dto.response.promocodes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.candle.store.orderservice.dto.response.Promocode;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllPromocodesResponse {

    private Boolean success;
    private List<Promocode> promocodes;
    private String errorCode;
    private String errorText;
}
