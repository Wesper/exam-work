package ru.candle.store.orderservice.dto.response.basket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.candle.store.orderservice.dto.Product;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplyPromocodeResponse {

    private Boolean success;
    private List<Product> products;
    private Long totalPrice;
    private Long totalPricePromo;
    private String errorCode;
    private String errorText;
}
