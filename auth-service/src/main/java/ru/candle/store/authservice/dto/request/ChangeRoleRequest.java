package ru.candle.store.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.candle.store.authservice.dictionary.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeRoleRequest {

    @NotBlank(message = "Имя пользователя не должно быть пустым")
    private String userName;

    private Role newRole;

}
