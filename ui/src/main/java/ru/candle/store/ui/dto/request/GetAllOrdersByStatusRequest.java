package ru.candle.store.ui.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import ru.candle.store.ui.dictionary.Status;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllOrdersByStatusRequest {

    @NonNull
    private Status status;
}
