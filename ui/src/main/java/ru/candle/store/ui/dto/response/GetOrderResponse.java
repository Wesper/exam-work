package ru.candle.store.ui.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.candle.store.ui.dictionary.Status;
import ru.candle.store.ui.dto.ProductEntity;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetOrderResponse {

    private Boolean success;
    private String firstName;
    private String lastName;
    private String address;
    private String date;
    private String promocode;
    private Long totalPrice;
    private Long totalPromoPrice;
    private List<ProductEntity> products;
    private Status status;
    private String errorCode;
    private String errorText;
}
