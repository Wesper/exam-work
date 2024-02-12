package ru.candle.store.ui.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import ru.candle.store.ui.dto.request.ApplyPromocodeRequest;

public interface IBasketService {

    String addProductToBasket(String productId, Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest);
    String getBasket(Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest);
    String deleteProductFromBasket(String productId, Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest);
    String clearBasket(Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest);
    String applyPromocodeToBasket(ApplyPromocodeRequest request, Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest);
    String getPreorder(String promocode, Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest);
}
