package ru.candle.store.ui.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.candle.store.ui.service.IOrderService;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService service;

    @GetMapping(value = "/create")
    public String createOrder(@RequestParam("promocode") String promocode, @RequestParam("address") String address,
                          Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        return service.createOrder(promocode, address, model, servletResponse, servletRequest);
    }

    @GetMapping(value = "/list")
    public String getUserOrders(Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        return service.getUserOrders(model, servletResponse, servletRequest);
    }

    @GetMapping(value = "/get/{orderId}")
    public String getUserOrder(@PathVariable("orderId") String orderId,  Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        return service.getUserOrder(orderId, model, servletResponse, servletRequest);
    }
}
