package ru.candle.store.profileservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetProfileResponse {

    private boolean success;
    private String firstName;
    private String lastName;
    private String middleName;
    private String city;
    private String birthday;
    private String address;

    private String errorCode;
    private String errorText;
}
