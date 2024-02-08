package ru.candle.store.productmplaceservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProductRequest {

    @NotNull
    private Long id;

    @Size(min = 1, max = 64, message = "Поле должно иметь размер от 1 до 64")
    @NotBlank
    private String imageId;

    @Size(min = 1, max = 128, message = "Поле должно иметь размер от 1 до 128")
    @NotBlank
    private String title;

    @Size(min = 1, max = 256, message = "Поле должно иметь размер от 1 до 256")
    @NotBlank
    private String description;

    @NotBlank
    private String subtitle;

    @Min(value = 0, message = "Цена должна быть положительным числом")
    @NotNull
    private Long price;

    @Size(min = 1, max = 32, message = "Поле должно иметь размер от 1 до 32")
    @NotBlank
    private String type;

    @Size(min = 1, max = 16, message = "Поле должно иметь размер от 1 до 16")
    @NotBlank
    private String measure;

    @Size(min = 1, max = 16, message = "Поле должно иметь размер от 1 до 16")
    @NotBlank
    private String unitMeasure;

    @NotNull
    private Boolean actual;

}
