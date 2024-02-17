package ru.candle.store.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasketProduct {

    private Long productId;
    private String imageId;
    private String title;
    private Long price;
    private Long promoPrice;
    private Long count;
}
