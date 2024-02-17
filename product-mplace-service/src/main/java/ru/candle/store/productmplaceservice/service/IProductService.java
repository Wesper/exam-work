package ru.candle.store.productmplaceservice.service;

import ru.candle.store.productmplaceservice.dto.request.*;
import ru.candle.store.productmplaceservice.dto.response.*;

public interface IProductService {

    /**
     * Получение списка продуктов
     * @return
     */
    GetAllProductsResponse getAllProducts();

    /**
     * Получение карточки продукта
     * @param rq запрос
     * @param userId идентификатор пользователя
     * @return карточка продукта
     */
    GetProductCardResponse getProductCard(GetProductCardRequest rq);

    /**
     * Добавление продукта
     * @param rq запрос
     * @return true или false
     */
    AddProductResponse addProduct(AddProductRequest rq);

    /**
     * Обновление продукта
     * @param rq запрос
     * @return true или false
     */
    UpdateProductResponse updateProduct(UpdateProductRequest rq);

    /**
     * Изменение статуса продукта
     * @param rq запрос
     * @return true или false
     */
    ChangeProductAvailableResponse changeProductAvailable(ChangeProductAvailableRequest rq);

    /**
     * Добавление отзыва о продукте
     * @param rq запрос
     * @param userId идентификатор пользователя
     * @param role роль пользователя
     * @return true или false
     */
    AddReviewResponse addReview(AddReviewRequest rq, Long userId, String role);

    /**
     * Добавление оценки продукта
     * @param rq запрос
     * @param userId идентификатор пользователя
     * @param role роль пользователя
     * @return true или false
     */
    AddRatingResponse addRating(AddRatingRequest rq, Long userId, String role);

    /**
     * Получение списка продуктов по идентификаторам
     * @param rq запрос
     * @return список продуктов
     */
    GetProductsInfoResponse getProductInfoByIds(GetProductsInfoRequest rq);

    /**
     * Удаление продукта
     * @param rq запрос
     * @return true или false
     */
    DeleteProductResponse deleteProduct(DeleteProductRequest rq);

    ProductIsAppreciatedResponse productIsAppreciated(ProductIsAppreciatedRequest rq, Long userId);
}
