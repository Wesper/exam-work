package ru.candle.store.orderservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.candle.store.orderservice.dto.request.order.*;
import ru.candle.store.orderservice.dto.response.order.*;
import ru.candle.store.orderservice.service.IOrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService service;

    /**
     * Создание заказа
     * @param rq запрос
     * @param role поль пользователя
     * @param userId идентификатор пользователя
     * @return true или false
     */
    @PreAuthorize("#role == 'USER' || #role == 'ADMIN' || #role == 'MANAGER'")
    @PostMapping(value = "/add")
    public AddOrderResponse addOrder(@RequestBody @Valid AddOrderRequest rq, @RequestHeader("role") String role, @RequestHeader("userId") Long userId) {
        return service.addOrder(rq, userId, role);
    }

    /**
     * Получение информации о заказе
     * @param rq запрос
     * @param role поль пользователя
     * @param userId идентификатор пользователя
     * @return информация о заказе
     */
    @PreAuthorize("#role == 'USER' || #role == 'ADMIN' || #role == 'MANAGER'")
    @PostMapping(value = "/get")
    public GetOrderResponse getOrder(@RequestBody @Valid GetOrderRequest rq, @RequestHeader("role") String role, @RequestHeader("userId") Long userId) {
        Boolean filterByUser = role.equals("USER");
        return service.getOrder(rq, userId, role, filterByUser);
    }

    /**
     * Получеиее списка заказов
     * @param role поль пользователя
     * @param userId идентификатор пользователя
     * @return список заказов
     */
    @PreAuthorize("#role == 'USER' || #role == 'ADMIN' || #role == 'MANAGER'")
    @GetMapping(value = "/get")
    public GetOrderListResponse getOrderList(@RequestHeader("role") String role, @RequestHeader("userId") Long userId) {
        return service.getOrderList(userId);
    }

    /**
     * Изменение статуса заказа
     * @param rq запрос
     * @param role поль пользователя
     * @return true или false
     */
    @PreAuthorize("#role == 'MANAGER' || #role == 'ADMIN'")
    @PostMapping(value = "/status/change")
    public ChangeOrderStatusResponse changeOrderStatus(@RequestBody @Valid ChangeOrderStatusRequest rq, @RequestHeader("role") String role) {
        return service.changeOrderStatus(rq, role);
    }

    /**
     * Получение всех заказов со статусом
     * @param rq запрос
     * @param role поль пользователя
     * @return список заказов
     */
    @PreAuthorize("#role == 'MANAGER' || #role == 'ADMIN'")
    @PostMapping(value = "/all/get")
    public GetAllOrdersByStatusResponse getAllOrders(@RequestBody @Valid GetAllOrdersByStatusRequest rq, @RequestHeader("role") String role) {
        return service.getAllOrdersByStatus(rq);
    }

    /**
     * Признак наличия продукта у клиента в списке купленных
     * @param rq запрос
     * @param role поль пользователя
     * @return список заказов
     */
    @PreAuthorize("#role == 'USER' || #role == 'MANAGER' || #role == 'ADMIN'")
    @PostMapping(value = "/purchased")
    public IsUserPurchasedProductResponse isUserPurchasedProduct(@RequestBody @Valid IsUserPurchasedProductRequest rq, @RequestHeader("role") String role, @RequestHeader("userId") String userId) {
        return service.isUserPurchasedProduct(rq, userId);
    }

}