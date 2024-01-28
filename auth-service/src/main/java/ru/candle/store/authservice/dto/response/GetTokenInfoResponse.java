package ru.candle.store.authservice.dto.response;

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
