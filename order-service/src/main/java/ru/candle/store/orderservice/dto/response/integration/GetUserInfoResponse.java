package ru.candle.store.orderservice.dto.response.integration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUserInfoResponse {

    private String firstName;
    private String lastName;
    private String middleName;
    private String city;
    private String birthday;
    private String address;
}
