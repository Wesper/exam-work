package ru.candle.store.productmplaceservice.service;

import ru.candle.store.productmplaceservice.dto.request.AddProductRequest;
import ru.candle.store.productmplaceservice.dto.request.ChangeProductAvailableRequest;
import ru.candle.store.productmplaceservice.dto.request.GetProductCardRequest;
import ru.candle.store.productmplaceservice.dto.request.UpdateProductRequest;
import ru.candle.store.productmplaceservice.dto.response.AddOrUpdateProductResponse;
import ru.candle.store.productmplaceservice.dto.response.ChangeProductAvailableResponse;
import ru.candle.store.productmplaceservice.dto.response.GetAllProductsResponse;
import ru.candle.store.productmplaceservice.dto.response.GetProductCardResponse;

public interface IProductService {

    GetAllProductsResponse getAllProducts();
    GetProductCardResponse getProductCard(GetProductCardRequest rq);
    AddOrUpdateProductResponse addProduct(AddProductRequest rq);
    AddOrUpdateProductResponse updateProduct(UpdateProductRequest rq);
    ChangeProductAvailableResponse changeProductAvailable(ChangeProductAvailableRequest rq);
}
