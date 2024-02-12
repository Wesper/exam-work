package ru.candle.store.ui.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.candle.store.ui.dto.BasketProduct;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplyPromocodeResponse {

    private Boolean success;
    private List<BasketProduct> products;
    private Long totalPrice;
    private Long totalPricePromo;
    private String errorCode;
    private String errorText;
}
