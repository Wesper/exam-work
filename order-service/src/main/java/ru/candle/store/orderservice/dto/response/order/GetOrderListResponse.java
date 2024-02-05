package ru.candle.store.orderservice.dto.response.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.candle.store.orderservice.dictionary.Status;
import ru.candle.store.orderservice.dto.Order;
import ru.candle.store.orderservice.dto.Product;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetOrderListResponse {

    private List<Order> orders;
}
