package ru.candle.store.productmplaceservice.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetProductCardRequest {

    @Min(value = 1, message = "Идентификатор должен быть числом больше одного")
    private Long productId;
}
