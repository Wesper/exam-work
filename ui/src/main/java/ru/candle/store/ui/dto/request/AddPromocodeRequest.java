package ru.candle.store.ui.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddPromocodeRequest {

    @NonNull
    private String promocode;
    @NonNull
    private Long percent;
    @NonNull
    private Boolean actual;
}
