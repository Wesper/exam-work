package ru.candle.store.orderservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.candle.store.orderservice.dto.request.order.AddOrderRequest;
import ru.candle.store.orderservice.dto.request.order.ChangeOrderStatusRequest;
import ru.candle.store.orderservice.dto.request.order.GetAllOrdersByStatusRequest;
import ru.candle.store.orderservice.dto.request.order.GetOrderRequest;
import ru.candle.store.orderservice.dto.response.order.*;
import ru.candle.store.orderservice.service.IOrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService service;

    @PreAuthorize("#role == 'USER'")
    @PostMapping(value = "/add")
    public AddOrderResponse addOrder(@RequestBody @Valid AddOrderRequest rq, @RequestHeader("role") String role, @RequestHeader("userId") Long userId) {
        return service.addOrder(rq, userId, role);
    }

    @PreAuthorize("#role == 'USER'")
    @PostMapping(value = "/get")
    public GetOrderResponse getOrder(@RequestBody @Valid GetOrderRequest rq, @RequestHeader("role") String role, @RequestHeader("userId") Long userId) {
        return service.getOrder(rq, userId, role);
    }

    @PreAuthorize("#role == 'USER'")
    @GetMapping(value = "/get")
    public GetOrderListResponse getOrderList(@RequestHeader("role") String role, @RequestHeader("userId") Long userId) {
        return service.getOrderList(userId);
    }

    @PreAuthorize("#role == 'MANAGER'")
    @PostMapping(value = "/status/change")
    public ChangeOrderStatusResponse changeOrderStatus(@RequestBody @Valid ChangeOrderStatusRequest rq, @RequestHeader("role") String role) {
        return service.changeOrderStatus(rq);
    }

    @PreAuthorize("#role == 'MANAGER'")
    @PostMapping(value = "/all/get")
    public GetAllOrdersByStatusResponse getAllOrders(@RequestBody @Valid GetAllOrdersByStatusRequest rq, @RequestHeader("role") String role) {
        return service.getAllOrdersByStatus(rq);
    }

}