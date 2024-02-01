package ru.candle.store.productmplaceservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetProductCardResponse {

    private Long productId;
    private String imageId;
    private String title;
    private String description;
    private Long price;
    private String measure;
    private String type;
    private Long rating;
    private Boolean actual;
    private List<Review> review;
}
