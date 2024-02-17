package ru.candle.store.orderservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.candle.store.orderservice.dto.request.basket.AddProductRequest;
import ru.candle.store.orderservice.dto.request.basket.ApplyPromocodeRequest;
import ru.candle.store.orderservice.dto.request.basket.ChangeCountProductRequest;
import ru.candle.store.orderservice.dto.request.basket.DeleteProductRequest;
import ru.candle.store.orderservice.dto.response.basket.*;
import ru.candle.store.orderservice.service.IBasketService;

@RestController
@RequestMapping("/basket")
public class BasketController {

    @Autowired
    private IBasketService service;

    /**
     * Добавление продукта в корзину
     * @param rq запрос
     * @param role роль пользователя
     * @param userId идентификатор пользователя
     * @return true или false
     */
    @PreAuthorize("#role == 'USER' || #role == 'ADMIN' || #role == 'MANAGER'")
    @PostMapping(value = "/add")
    public AddProductResponse addProduct(@RequestBody @Valid AddProductRequest rq, @RequestHeader("role") String role,
                                         @RequestHeader("userId") Long userId, HttpServletRequest servletRequest) {
        return service.addProduct(rq, userId, role);
    }

    /**
     * Удаление продукта из корзины
     * @param rq запрос
     * @param role роль пользователя
     * @param userId идентификатор пользователя
     * @return true или false
     */
    @PreAuthorize("#role == 'USER' || #role == 'ADMIN' || #role == 'MANAGER'")
    @PostMapping(value = "/delete")
    public DeleteProductResponse deleteProduct(@RequestBody @Valid DeleteProductRequest rq, @RequestHeader("role") String role, @RequestHeader("userId") Long userId) {
        return service.deleteProduct(rq, userId);
    }

    /**
     * Изменение количества позиций продукта в корзине
     * @param rq запрос
     * @param role роль пользователя
     * @param userId идентификатор пользователя
     * @return true или false
     */
    @PreAuthorize("#role == 'USER' || #role == 'ADMIN' || #role == 'MANAGER'")
    @PostMapping(value = "/product/count/change")
    public ChangeCountProductResponse changeCountProduct(@RequestBody @Valid ChangeCountProductRequest rq, @RequestHeader("role") String role, @RequestHeader("userId") Long userId) {
        return service.changeCountProduct(rq, userId);
    }

    /**
     * Удаление всех продуктов из корзины
     * @param role роль пользователя
     * @param userId идентификатор пользователя
     * @return true или false
     */
    @PreAuthorize("#role == 'USER' || #role == 'ADMIN' || #role == 'MANAGER'")
    @GetMapping(value = "/clear")
    public DeleteAllProductResponse deleteAllProduct(@RequestHeader("role") String role, @RequestHeader("userId") Long userId) {
        return service.deleteAllProduct(userId);
    }

    /**
     * Получение продуктов в корзине
     * @param role роль пользователя
     * @param userId идентификатор пользователя
     * @return список продуктов
     */
    @PreAuthorize("#role == 'USER' || #role == 'ADMIN' || #role == 'MANAGER'")
    @GetMapping(value = "/get")
    public GetBasketResponse getBasket(@RequestHeader("role") String role, @RequestHeader("userId") Long userId) {
        return service.getBasket(userId, role);
    }

    /**
     * Применение промокода для товаров корзины
     * @param rq запрос
     * @param role роль пользователя
     * @param userId идентификатор пользователя
     * @return список продуктов с учетом промокода
     */
    @PreAuthorize("#role == 'USER' || #role == 'ADMIN' || #role == 'MANAGER'")
    @PostMapping(value = "/promocode/apply")
    public ApplyPromocodeResponse applyPromocode(@RequestBody @Valid ApplyPromocodeRequest rq, @RequestHeader("role") String role, @RequestHeader("userId") Long userId) {
        return service.applyPromocode(rq, userId, role);
    }
}
