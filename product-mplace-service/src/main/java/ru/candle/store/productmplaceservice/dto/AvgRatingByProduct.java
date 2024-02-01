package ru.candle.store.productmplaceservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvgRatingByProduct {

    private Double avgRating;
    private Long productId;
}
