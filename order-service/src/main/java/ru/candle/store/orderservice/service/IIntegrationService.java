package ru.candle.store.orderservice.service;

import ru.candle.store.orderservice.dto.response.integration.GetProductsInfoResponse;
import ru.candle.store.orderservice.dto.response.integration.GetUserAuthResponse;
import ru.candle.store.orderservice.dto.response.integration.GetUserInfoResponse;
import ru.candle.store.orderservice.exception.OrderException;

import java.util.List;

public interface IIntegrationService {

    /**
     * Получение информации о продуктах по идентификаторам
     * @param productIds идентификаторы продуктов
     * @param role роль пользователя
     * @return информаци о продуктах
     * @throws OrderException
     */
    GetProductsInfoResponse getProductInfoByIds(List<Long> productIds, String role) throws OrderException;

    /**
     * Получение информации о пользователе
     * @param userId идентификатор пользователя
     * @param role роль пользователя
     * @return информаци о пользователе
     * @throws OrderException
     */
    GetUserInfoResponse getUserInfo(Long userId, String role) throws OrderException;

    /**
     * Получение информации об авторизации пользователя
     * @param userId идентификатор пользователя
     * @param role роль пользователя
     * @return информаци о пользователе
     * @throws OrderException
     */
    GetUserAuthResponse getUserAuth(Long userId, String role) throws OrderException;
}