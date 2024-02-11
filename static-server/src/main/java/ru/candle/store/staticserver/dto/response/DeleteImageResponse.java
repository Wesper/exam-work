package ru.candle.store.staticserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteImageResponse {

    private Boolean success;
    private String errorCode;
    private String errorText;
}
