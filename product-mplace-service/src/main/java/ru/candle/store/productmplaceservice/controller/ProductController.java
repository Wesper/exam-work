package ru.candle.store.productmplaceservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.candle.store.productmplaceservice.dto.request.AddProductRequest;
import ru.candle.store.productmplaceservice.dto.request.ChangeProductAvailableRequest;
import ru.candle.store.productmplaceservice.dto.request.GetProductCardRequest;
import ru.candle.store.productmplaceservice.dto.request.UpdateProductRequest;
import ru.candle.store.productmplaceservice.dto.response.AddOrUpdateProductResponse;
import ru.candle.store.productmplaceservice.dto.response.ChangeProductAvailableResponse;
import ru.candle.store.productmplaceservice.dto.response.GetAllProductsResponse;
import ru.candle.store.productmplaceservice.dto.response.GetProductCardResponse;
import ru.candle.store.productmplaceservice.service.IProductService;


@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    IProductService productService;

    @GetMapping(value = "/get")
    public GetAllProductsResponse getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping(value = "/card")
    public GetProductCardResponse getProductCard(@RequestBody @Valid GetProductCardRequest rq) {
        return productService.getProductCard(rq);
    }

    @PostMapping(value = "/add")
    public AddOrUpdateProductResponse addProduct(@RequestBody @Valid AddProductRequest rq) {
        return productService.addProduct(rq);
    }

    @PostMapping(value = "/update")
    public AddOrUpdateProductResponse updateProduct(@RequestBody @Valid UpdateProductRequest rq) {
        return productService.updateProduct(rq);
    }

    @PostMapping(value = "/available/change")
    public ChangeProductAvailableResponse changeProductAvailable(@RequestBody @Valid ChangeProductAvailableRequest rq) {
        return productService.changeProductAvailable(rq);
    }

    //TODO: Добавить работу с ошибками

}
