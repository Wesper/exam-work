package ru.candle.store.orderservice.service;

import ru.candle.store.orderservice.dto.request.basket.AddProductRequest;
import ru.candle.store.orderservice.dto.request.basket.ApplyPromocodeRequest;
import ru.candle.store.orderservice.dto.request.basket.ChangeCountProductRequest;
import ru.candle.store.orderservice.dto.request.basket.DeleteProductRequest;
import ru.candle.store.orderservice.dto.response.basket.*;

public interface IBasketService {

    AddProductResponse addProduct(AddProductRequest rq, Long userId, String role);
    DeleteProductResponse deleteProduct(DeleteProductRequest rq, Long userId);
    ChangeCountProductResponse changeCountProduct(ChangeCountProductRequest rq, Long userId);
    DeleteAllProductResponse deleteAllProduct(Long userId);
    GetBasketResponse getBasket(Long userId, String role);
    ApplyPromocodeResponse applyPromocode(ApplyPromocodeRequest rq, Long userId, String role);
}
