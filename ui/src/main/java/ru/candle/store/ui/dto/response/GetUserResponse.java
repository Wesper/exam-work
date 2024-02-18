package ru.candle.store.ui.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUserResponse {

    private boolean success;
    private String userId;
    private String userName;
    private String email;
    private String role;
    private String errorCode;
    private String errorText;

}
