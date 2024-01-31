package ru.candle.store.gatewayserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTokenInfoResponse {

    private String username;
    private String email;
    private String role;

}
