package ru.candle.store.ui.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IsUserPurchasedProductResponse {

    private Boolean success;
    private Boolean isPurchased;
    private String errorCode;
    private String errorText;
}
