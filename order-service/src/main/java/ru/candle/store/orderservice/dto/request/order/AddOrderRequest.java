package ru.candle.store.orderservice.dto.request.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import ru.candle.store.orderservice.dto.request.ProductAndCount;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddOrderRequest {

    @NonNull
    private String address;
    private String promocode;
    @NonNull
    private List<ProductAndCount> productsAndCounts;
}
