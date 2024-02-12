package ru.candle.store.ui.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.candle.store.ui.dto.Promocode;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllPromocodesResponse {

    private Boolean success;
    private List<Promocode> promocodes;
    private String errorCode;
    private String errorText;
}
