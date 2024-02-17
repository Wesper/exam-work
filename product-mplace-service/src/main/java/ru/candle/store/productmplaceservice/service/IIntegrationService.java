package ru.candle.store.productmplaceservice.service;

import ru.candle.store.productmplaceservice.exception.ProductMplaceException;

public interface IIntegrationService {

    /**
     * Признак приобретения продукта пользователем
     * @param productId идентификатор продукта
     * @param userId идентификатор пользователя
     * @param role роль пользователя
     * @return признак приобретения продукта
     */
    boolean isUserPurchasedProduct(Long productId, Long userId, String role) throws ProductMplaceException;
}
