package ru.candle.store.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateRequest {

    @NotBlank(message = "Токен не должен быть пустым")
    private String token;
}
