package ru.candle.store.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductInfo {

    private Long productId;
    private String imageId;
    private String title;
    private String description;
    private String subtitle;
    private Long price;
    private String type;
    private String measure;
    private String unitMeasure;
    private Boolean actual;
}
