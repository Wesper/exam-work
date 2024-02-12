package ru.candle.store.ui.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.candle.store.ui.dto.request.ApplyPromocodeRequest;
import ru.candle.store.ui.service.IBasketService;

@Controller
@RequestMapping("/basket")
public class BasketController {

    @Autowired
    private IBasketService service;

    @GetMapping("/add/{productId}")
    public String addProductToBasket(@PathVariable String productId, Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        return service.addProductToBasket(productId, model, servletResponse, servletRequest);
    }

    @GetMapping()
    public String getBasket(Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        return service.getBasket(model, servletResponse, servletRequest);
    }

    @GetMapping("/product/delete/{productId}")
    public String deleteProductFromBasket(@PathVariable String productId, Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        return service.deleteProductFromBasket(productId, model, servletResponse, servletRequest);
    }

    @GetMapping("/clear")
    public String clearBasket(Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        return service.clearBasket(model, servletResponse, servletRequest);
    }

    @PostMapping("/promocode/apply")
    public String applyPromocodeToBasket(@ModelAttribute ApplyPromocodeRequest request, Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        return service.applyPromocodeToBasket(request, model, servletResponse, servletRequest);
    }

    @GetMapping("/preorder")
    public String getPreorder(@RequestParam String promocode, Model model, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        return service.getPreorder(promocode, model, servletResponse, servletRequest);
    }
}
