package ru.candle.store.productmplaceservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    private Long productId;
    private String imageId;
    private String title;
    private String subtitle;
    private Long price;
    private String type;
    private Double rating;
}
