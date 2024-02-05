package ru.candle.store.orderservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.candle.store.orderservice.dto.request.promocodes.AddPromocodeRequest;
import ru.candle.store.orderservice.dto.request.promocodes.ChangePromocodeActualRequest;
import ru.candle.store.orderservice.dto.response.promocodes.AddPromocodeResponse;
import ru.candle.store.orderservice.dto.response.promocodes.ChangePromocodeActualResponse;
import ru.candle.store.orderservice.dto.response.promocodes.GetAllPromocodesResponse;
import ru.candle.store.orderservice.dto.response.promocodes.GetPromocodeResponse;
import ru.candle.store.orderservice.service.IPromocodeService;

@RestController
@RequestMapping("/promocode")
public class PromocodeController {

    @Autowired
    private IPromocodeService service;

    @PreAuthorize("#role == 'ADMIN'")
    @PostMapping(value = "/add")
    public AddPromocodeResponse addPromocode(@RequestBody @Valid AddPromocodeRequest rq, @RequestHeader("role") String role) {
        return service.addPromocode(rq);
    }

    @PreAuthorize("#role == 'USER'")
    @GetMapping(value = "/get/{promocode}")
    public GetPromocodeResponse getPromocode(@PathVariable("promocode") String promocode, @RequestHeader("role") String role) {
        return service.getPromocode(promocode);
    }

    @PreAuthorize("#role == 'ADMIN'")
    @GetMapping(value = "/get")
    public GetAllPromocodesResponse getAllPromocodes(@RequestHeader("role") String role) {
        return service.getAllPromocodes();
    }

    @PreAuthorize("#role == 'ADMIN'")
    @PostMapping(value = "/actual/change")
    public ChangePromocodeActualResponse changePromocodeActual(@RequestBody @Valid ChangePromocodeActualRequest rq, @RequestHeader("role") String role) {
        return service.changePromocodeActual(rq);
    }
}
