package ru.candle.store.orderservice.dto.response.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.candle.store.orderservice.dictionary.Status;
import ru.candle.store.orderservice.dto.Product;
import ru.candle.store.orderservice.entity.ProductEntity;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetOrderResponse {

    private String firstName;
    private String lastName;
    private String address;
    private LocalDateTime date;
    private String promocode;
    private Long totalPrice;
    private Long totalPromoPrice;
    private List<ProductEntity> products;
    private Status status;

}
