package ru.candle.store.productmplaceservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeProductAvailableRequest {

    @Min(value = 1, message = "Идентификатор должен быть числом больше одного")
    @NotNull
    private Long productId;

    @NotNull
    private boolean actual;
}
