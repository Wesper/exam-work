package ru.candle.store.productmplaceservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.candle.store.productmplaceservice.dto.Review;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetProductCardResponse {

    private Boolean success;
    private Long productId;
    private String imageId;
    private String title;
    private String description;
    private Long price;
    private String measure;
    private String unitMeasure;
    private String type;
    private Double rating;
    private Boolean actual;
    private Boolean appreciated;
    private List<Review> review;
    private String errorCode;
    private String errorText;
}
