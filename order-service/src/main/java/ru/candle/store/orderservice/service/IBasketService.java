package ru.candle.store.orderservice.service;

import ru.candle.store.orderservice.dto.request.basket.AddProductRequest;
import ru.candle.store.orderservice.dto.request.basket.ApplyPromocodeRequest;
import ru.candle.store.orderservice.dto.request.basket.ChangeCountProductRequest;
import ru.candle.store.orderservice.dto.request.basket.DeleteProductRequest;
import ru.candle.store.orderservice.dto.response.basket.*;

public interface IBasketService {

    /**
     * Добавление продукта в корзинц
     * @param rq запрос
     * @param userId идентификатор пользователя
     * @param role роль пользователя
     * @return true или false
     */
    AddProductResponse addProduct(AddProductRequest rq, Long userId, String role);

    /**
     * Удаление продуктв из корзины
     * @param rq запрос
     * @param userId идентификатор пользователя
     * @return true или false
     */
    DeleteProductResponse deleteProduct(DeleteProductRequest rq, Long userId);

    /**
     * Изменение количества позиций товара в корзине
     * @param rq запрос
     * @param userId идентификатор пользователя
     * @return true или false
     */
    ChangeCountProductResponse changeCountProduct(ChangeCountProductRequest rq, Long userId);

    /**
     * Удаление всех продуктов из корзины
     * @param userId идентификатор пользователя
     * @return true или false
     */
    DeleteAllProductResponse deleteAllProduct(Long userId);

    /**
     * Полученеи списка продуктов корзины
     * @param userId идентификатор пользователя
     * @param role роль пользователя
     * @return список продуктов
     */
    GetBasketResponse getBasket(Long userId, String role);

    /**
     * Применение промокода к товарам в корзине
     * @param rq запрос
     * @param userId идентификатор пользователя
     * @param role роль пользователя
     * @return список товаров с учетом промокода
     */
    ApplyPromocodeResponse applyPromocode(ApplyPromocodeRequest rq, Long userId, String role);
}
