package ru.candle.store.ui.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.candle.store.ui.dto.Image;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetImagesListResponse {

    private Boolean success;
    private List<Image> images;
    private String errorCode;
    private String errorText;
}
