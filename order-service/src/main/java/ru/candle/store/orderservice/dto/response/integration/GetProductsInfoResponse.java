package ru.candle.store.orderservice.dto.response.integration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.candle.store.orderservice.dto.response.ProductInfo;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetProductsInfoResponse {

    private List<ProductInfo> productsInfo;
}
