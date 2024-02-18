package ru.candle.store.authservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeRoleResponse {

    private boolean success;
    private String errorCode;
    private String errorText;
}
