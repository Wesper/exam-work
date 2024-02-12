package ru.candle.store.ui.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePromocodeActualRequest {

    private String promocode;
    private boolean actual;
}
