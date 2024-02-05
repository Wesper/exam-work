package ru.candle.store.orderservice.controller;

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

    @PreAuthorize("#role == 'USER'")
    @PostMapping(value = "/add")
    public AddProductResponse addProduct(@RequestBody @Valid AddProductRequest rq, @RequestHeader("role") String role, @RequestHeader("userId") Long userId) {
        return service.addProduct(rq, userId, role);
    }

    @PreAuthorize("#role == 'USER'")
    @PostMapping(value = "/delete")
    public DeleteProductResponse deleteProduct(@RequestBody @Valid DeleteProductRequest rq, @RequestHeader("role") String role, @RequestHeader("userId") Long userId) {
        return service.deleteProduct(rq, userId);
    }

    @PreAuthorize("#role == 'USER'")
    @PostMapping(value = "/product/count/change")
    public ChangeCountProductResponse changeCountProduct(@RequestBody @Valid ChangeCountProductRequest rq, @RequestHeader("role") String role, @RequestHeader("userId") Long userId) {
        return service.changeCountProduct(rq, userId);
    }

    @PreAuthorize("#role == 'USER'")
    @PostMapping(value = "/clear")
    public DeleteAllProductResponse deleteAllProduct(@RequestHeader("role") String role, @RequestHeader("userId") Long userId) {
        return service.deleteAllProduct(userId);
    }

    @PreAuthorize("#role == 'USER'")
    @GetMapping(value = "/get")
    public GetBasketResponse getBasket(@RequestHeader("role") String role, @RequestHeader("userId") Long userId) {
        return service.getBasket(userId, role);
    }

    @PreAuthorize("#role == 'USER'")
    @PostMapping(value = "/promocode/apply")
    public ApplyPromocodeResponse applyPromocode(@RequestBody @Valid ApplyPromocodeRequest rq, @RequestHeader("role") String role, @RequestHeader("userId") Long userId) {
        return service.applyPromocode(rq, userId, role);
    }
}
