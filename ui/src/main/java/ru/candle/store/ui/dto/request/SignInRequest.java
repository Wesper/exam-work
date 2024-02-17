package ru.candle.store.ui.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignInRequest {

    @Size(min = 1, max = 64, message = "Логин должен содержать от 1 до 64 символов")
    @NotBlank(message = "Логин не должен быть пустым")
    private String username;

    @Size(min = 6, max = 255, message = "Пароль должен содержать от 6 до 255 символов")
    @NotBlank(message = "Пароль не должен быть пустым")
    private String password;

}
