package ru.candle.store.productmplaceservice.service;

public interface IIntegrationService {

    boolean isUserPurchasedProduct(Long productId, Long userId);
}
