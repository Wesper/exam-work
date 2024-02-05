package ru.candle.store.productmplaceservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.candle.store.productmplaceservice.dto.request.*;
import ru.candle.store.productmplaceservice.dto.response.*;
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
    public GetProductCardResponse getProductCard(@RequestBody @Valid GetProductCardRequest rq, @RequestHeader("userId") Long userId) {
        return productService.getProductCard(rq, userId);
    }

    @PreAuthorize("#role == 'MANAGER'")
    @PostMapping(value = "/info/get")
    public GetProductsInfoResponse getProductInfoCard(@RequestBody @Valid GetProductsInfoRequest rq, @RequestHeader("role") String role) {
        return productService.getProductInfoByIds(rq);
    }

    @PreAuthorize("#role == 'ADMIN'")
    @PostMapping(value = "/add")
    public AddProductResponse addProduct(@RequestBody @Valid AddProductRequest rq, @RequestHeader("role") String role) {
        return productService.addProduct(rq);
    }

    @PreAuthorize("#role == 'ADMIN'")
    @PostMapping(value = "/update")
    public UpdateProductResponse updateProduct(@RequestBody @Valid UpdateProductRequest rq, @RequestHeader("role") String role) {
        return productService.updateProduct(rq);
    }

    @PreAuthorize("#role == 'ADMIN'")
    @PostMapping(value = "/available/change")
    public ChangeProductAvailableResponse changeProductAvailable(@RequestBody @Valid ChangeProductAvailableRequest rq, @RequestHeader("role") String role) {
        return productService.changeProductAvailable(rq);
    }

    @PreAuthorize("#role == 'USER'")
    @PostMapping(value = "/review/add")
    public AddReviewResponse addReview(@RequestBody @Valid AddReviewRequest rq, @RequestHeader("userId") Long userId, @RequestHeader("role") String role) {
        return productService.addReview(rq, userId);
    }

    @PreAuthorize("#role == 'USER'")
    @PostMapping(value = "/rating/add")
    public AddRatingResponse addProduct(@RequestBody @Valid AddRatingRequest rq, @RequestHeader("userId") Long userId, @RequestHeader("role") String role) {
        return productService.addRating(rq, userId);
    }

    //TODO: Добавить работу с ошибками

}
