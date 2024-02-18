package ru.candle.store.ui.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.candle.store.ui.dictionary.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeRoleRequest {

    private String userName;

    private Role newRole;

}
