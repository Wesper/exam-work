package ru.candle.store.productmplaceservice.service;

import ru.candle.store.productmplaceservice.dto.request.*;
import ru.candle.store.productmplaceservice.dto.response.*;

public interface IProductService {

    GetAllProductsResponse getAllProducts();
    GetProductCardResponse getProductCard(GetProductCardRequest rq, Long userId);
    AddProductResponse addProduct(AddProductRequest rq);
    UpdateProductResponse updateProduct(UpdateProductRequest rq);
    ChangeProductAvailableResponse changeProductAvailable(ChangeProductAvailableRequest rq);
    AddReviewResponse addReview(AddReviewRequest rq, Long userId);
    AddRatingResponse addRating(AddRatingRequest rq, Long userId);
}
