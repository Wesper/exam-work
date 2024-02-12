package ru.candle.store.ui.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import ru.candle.store.ui.dto.ProductAndCount;

import java.util.List;

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
