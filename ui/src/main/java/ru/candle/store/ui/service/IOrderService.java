package ru.candle.store.ui.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;

public interface IOrderService {

    String createOrder(String promocode, String address,
                       Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest);
    String getUserOrders(Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest);
    String getUserOrder(String orderId, Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest);
}
