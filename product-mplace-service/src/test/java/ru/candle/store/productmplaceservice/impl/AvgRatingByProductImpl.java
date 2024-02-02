package ru.candle.store.productmplaceservice.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.candle.store.productmplaceservice.dto.IAvgRatingByProduct;

@Data
@AllArgsConstructor
public class AvgRatingByProductImpl implements IAvgRatingByProduct {

    private Double average;
    private Long id;

    @Override
    public Double getAverage() {
        return this.average;
    }

    @Override
    public Long getId() {
        return this.id;
    }
}
