package ru.candle.store.ui.dto.request;

import jakarta.validation.constraints.Max;
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
public class AddRatingRequest {

    @Min(value = 1, message = "Идентификатор должен быть числом больше одного")
    @NotNull
    private Long productId;

    @Min(value = 1, message = "Идентификатор должен быть от 0 до 5")
    @Max(value = 5, message = "Идентификатор должен быть от 0 до 5")
    @NotNull
    private Long rating;

}
