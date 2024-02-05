package ru.candle.store.orderservice.service;

import ru.candle.store.orderservice.dto.response.integration.GetProductsInfoResponse;
import ru.candle.store.orderservice.dto.response.integration.GetUserInfoResponse;

import java.util.List;

public interface IIntegrationService {

    GetProductsInfoResponse getProductInfoByIds(List<Long> productIds, String role);
    GetUserInfoResponse getUserInfo(Long userId, String role);
}