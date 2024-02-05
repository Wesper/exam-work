package ru.candle.store.gatewayserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTokenInfoResponse {

    private Long userId;
    private String username;
    private String email;
    private String role;

}
