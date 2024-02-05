package ru.candle.store.orderservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Promocode {

    private String promocode;
    private Long percent;
    private Boolean actual;
}
