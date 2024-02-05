package ru.candle.store.orderservice.service;

import ru.candle.store.orderservice.dto.request.order.AddOrderRequest;
import ru.candle.store.orderservice.dto.request.order.ChangeOrderStatusRequest;
import ru.candle.store.orderservice.dto.request.order.GetAllOrdersByStatusRequest;
import ru.candle.store.orderservice.dto.request.order.GetOrderRequest;
import ru.candle.store.orderservice.dto.response.order.*;

public interface IOrderService {

    AddOrderResponse addOrder(AddOrderRequest rq, Long userId, String role);
    GetOrderResponse getOrder(GetOrderRequest rq, Long userId, String role);
    ChangeOrderStatusResponse changeOrderStatus(ChangeOrderStatusRequest rq);
    GetAllOrdersByStatusResponse getAllOrdersByStatus(GetAllOrdersByStatusRequest rq);
    GetOrderListResponse getOrderList(Long userId);
}
