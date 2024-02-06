package ru.candle.store.profileservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveProfileResponse {

    private boolean success;
    private String errorCode;
    private String errorText;

}
