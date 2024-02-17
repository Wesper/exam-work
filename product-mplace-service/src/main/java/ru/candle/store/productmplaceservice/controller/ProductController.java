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

    /**
     * Получение списка продуктов
     * @return
     */
    @GetMapping(value = "/get")
    public GetAllProductsResponse getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * Получение карточки продукта
     * @param rq запрос
     * @return карточка продукта
     */
    @PostMapping(value = "/card")
    public GetProductCardResponse getProductCard(@RequestBody @Valid GetProductCardRequest rq) {
        return productService.getProductCard(rq);
    }

    /**
     * Получение списка продуктов по идентификаторам
     * @param rq запрос
     * @param role роль пользователя
     * @return список продуктов
     */
    @PreAuthorize("#role == 'USER' || #role == 'ADMIN' || #role == 'MANAGER'")
    @PostMapping(value = "/info/get")
    public GetProductsInfoResponse getProductInfoByIds(@RequestBody @Valid GetProductsInfoRequest rq, @RequestHeader("role") String role) {
        return productService.getProductInfoByIds(rq);
    }

    /**
     * Получение признака оценки продукта
     * @param rq запрос
     * @param role роль пользователя
     * @return список продуктов
     */
    @PreAuthorize("#role == 'USER' || #role == 'ADMIN' || #role == 'MANAGER'")
    @PostMapping(value = "/appreciated")
    public ProductIsAppreciatedResponse productIsAppreciated(@RequestBody @Valid ProductIsAppreciatedRequest rq, @RequestHeader("role") String role, @RequestHeader(value = "userId", required = false) Long userId) {
        return productService.productIsAppreciated(rq, userId);
    }

    /**
     * Добавление продукта
     * @param rq запрос
     * @param role роль пользователя
     * @return true или false
     */
    @PreAuthorize("#role == 'ADMIN'")
    @PostMapping(value = "/add")
    public AddProductResponse addProduct(@RequestBody @Valid AddProductRequest rq, @RequestHeader("role") String role) {
        return productService.addProduct(rq);
    }

    /**
     * Обновление продукта
     * @param rq запрос
     * @param role роль пользователя
     * @return true или false
     */
    @PreAuthorize("#role == 'ADMIN'")
    @PostMapping(value = "/update")
    public UpdateProductResponse updateProduct(@RequestBody @Valid UpdateProductRequest rq, @RequestHeader("role") String role) {
        return productService.updateProduct(rq);
    }

    /**
     * Удаление продукта
     * @param rq запрос
     * @param role роль пользователя
     * @return true или false
     */
    @PreAuthorize("#role == 'ADMIN'")
    @PostMapping(value = "/delete")
    public DeleteProductResponse deleteProduct(@RequestBody @Valid DeleteProductRequest rq, @RequestHeader("role") String role) {
        return productService.deleteProduct(rq);
    }

    /**
     * Изменение статуса продукта
     * @param rq запрос
     * @param role роль пользователя
     * @return true или false
     */
    @PreAuthorize("#role == 'ADMIN'")
    @PostMapping(value = "/available/change")
    public ChangeProductAvailableResponse changeProductAvailable(@RequestBody @Valid ChangeProductAvailableRequest rq, @RequestHeader("role") String role) {
        return productService.changeProductAvailable(rq);
    }

    /**
     * Добавление отзыва о продукте
     * @param rq запрос
     * @param userId идентификатор пользователя
     * @param role роль пользователя
     * @return true или false
     */
    @PreAuthorize("#role == 'USER' || #role == 'ADMIN' || #role == 'MANAGER'")
    @PostMapping(value = "/review/add")
    public AddReviewResponse addReview(@RequestBody @Valid AddReviewRequest rq, @RequestHeader("userId") Long userId, @RequestHeader("role") String role) {
        return productService.addReview(rq, userId, role);
    }

    /**
     * Добавление оценки продукта
     * @param rq запрос
     * @param userId идентификатор пользователя
     * @param role роль пользователя
     * @return true или false
     */
    @PreAuthorize("#role == 'USER' || #role == 'ADMIN' || #role == 'MANAGER'")
    @PostMapping(value = "/rating/add")
    public AddRatingResponse addRating(@RequestBody @Valid AddRatingRequest rq, @RequestHeader("userId") Long userId, @RequestHeader("role") String role) {
        return productService.addRating(rq, userId, role);
    }
}
