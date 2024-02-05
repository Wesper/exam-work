package ru.candle.store.orderservice.dto.response.promocodes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePromocodeActualResponse {

    private boolean success;
}
