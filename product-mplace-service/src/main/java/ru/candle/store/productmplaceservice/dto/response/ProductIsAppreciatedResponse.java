package ru.candle.store.productmplaceservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductIsAppreciatedResponse {

    private Boolean success;
    private Boolean appreciated;
    private String errorCode;
    private String errorText;
}
