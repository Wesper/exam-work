package ru.candle.store.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserRequest {

    @NotBlank(message = "Имя пользователя не должно быть пустым")
    private String userName;


}
