package ru.candle.store.productmplaceservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.candle.store.productmplaceservice.dto.ProductInfo;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetProductsInfoResponse {

    private Boolean success;
    private List<ProductInfo> productsInfo;
    private String errorCode;
    private String errorText;
}
