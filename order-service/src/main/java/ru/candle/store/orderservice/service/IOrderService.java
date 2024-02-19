package ru.candle.store.orderservice.service;

import ru.candle.store.orderservice.dto.request.order.*;
import ru.candle.store.orderservice.dto.response.order.*;

public interface IOrderService {

    /**
     * Добавление заказа
     * @param rq запрос
     * @param userId идентификатор пользователя
     * @param role роль пользователя
     * @return true или false
     */
    AddOrderResponse addOrder(AddOrderRequest rq, Long userId, String role);

    /**
     * Получение информации о заказе
     * @param rq запрос
     * @param userId идентификатор пользователя
     * @param role роль пользователя
     * @return информация о заказе
     */
    GetOrderResponse getOrder(GetOrderRequest rq, Long userId, String role, Boolean filterByUser);

    /**
     * Изменение статуса заказа
     * @param rq запрос
     * @return true или false
     */
    ChangeOrderStatusResponse changeOrderStatus(ChangeOrderStatusRequest rq, String role);

    /**
     * Получение списка заказов со статусом
     * @param rq запрос
     * @return список заказов
     */
    GetAllOrdersByStatusResponse getAllOrdersByStatus(GetAllOrdersByStatusRequest rq);

    /**
     * Получение списка заказов
     * @param userId идентификатор пользователя
     * @return список заказов
     */
    GetOrderListResponse getOrderList(Long userId);

    /**
     * Признак приобретения продукта пользователем
     * @param rq запрос
     * @return признак приобретения
     */
    IsUserPurchasedProductResponse isUserPurchasedProduct(IsUserPurchasedProductRequest rq, String userId);
}
