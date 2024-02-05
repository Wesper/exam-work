package ru.candle.store.orderservice;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.candle.store.orderservice.dto.Product;
import ru.candle.store.orderservice.dto.request.basket.AddProductRequest;
import ru.candle.store.orderservice.dto.request.basket.ApplyPromocodeRequest;
import ru.candle.store.orderservice.dto.request.basket.ChangeCountProductRequest;
import ru.candle.store.orderservice.dto.request.basket.DeleteProductRequest;
import ru.candle.store.orderservice.dto.response.ProductInfo;
import ru.candle.store.orderservice.dto.response.basket.*;
import ru.candle.store.orderservice.dto.response.integration.GetProductsInfoResponse;
import ru.candle.store.orderservice.entity.BasketEntity;
import ru.candle.store.orderservice.entity.PromocodeEntity;
import ru.candle.store.orderservice.repository.BasketRepository;
import ru.candle.store.orderservice.repository.PromocodeRepository;
import ru.candle.store.orderservice.service.IIntegrationService;
import ru.candle.store.orderservice.service.impl.BasketServiceImpl;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class BasketServiceTest {

    @Mock
    private IIntegrationService integrationService;

    @Mock
    private BasketRepository basketRepository;

    @Mock
    private PromocodeRepository promocodeRepository;

    @InjectMocks
    private BasketServiceImpl basketService;

    @Test
    void whenAddProductSuccess() {
        AddProductRequest rq = new AddProductRequest(1L, 1L);
        GetProductsInfoResponse productInfoResponse = new GetProductsInfoResponse(List.of(new ProductInfo(1L, "image", "title", "description", "subtitle", 1L, "type", "measure", "unit", true)));
        Mockito.when(integrationService.getProductInfoByIds(List.of(1L), "USER")).thenReturn(productInfoResponse);
        BasketEntity entity = new BasketEntity(1L, 1L, 1L);
        Mockito.when(basketRepository.save(entity)).thenReturn(entity);

        AddProductResponse rs = basketService.addProduct(rq, 1L, "USER");
        Assertions.assertEquals(new AddProductResponse(true), rs);
    }

    @Test
    void whenAddProductFail() {
        AddProductRequest rq = new AddProductRequest(1L, 1L);
        Mockito.when(integrationService.getProductInfoByIds(List.of(1L), "USER")).thenThrow(RuntimeException.class);

        Assertions.assertThrows(RuntimeException.class, () -> basketService.addProduct(rq, 1L, "USER"));
    }

    @Test
    void whenDeleteProductSuccess() {
        Mockito.when(basketRepository.existsBasketEntityByProductIdAndUserId(1L, 1L)).thenReturn(true);
        Mockito.when(basketRepository.deleteByProductIdAndUserId(1L, 1L)).thenReturn(1);

        Assertions.assertEquals(new DeleteProductResponse(true), basketService.deleteProduct(new DeleteProductRequest(1L), 1L));
    }

    @Test
    void whenDeleteProductNotFoundFail() {
        Mockito.when(basketRepository.existsBasketEntityByProductIdAndUserId(1L, 1L)).thenReturn(false);

        Assertions.assertThrows(RuntimeException.class, () -> basketService.deleteProduct(new DeleteProductRequest(1L), 1L));
    }

    @Test
    void whenDeleteProductFail() {
        Mockito.when(basketRepository.existsBasketEntityByProductIdAndUserId(1L, 1L)).thenReturn(true);
        Mockito.when(basketRepository.deleteByProductIdAndUserId(1L, 1L)).thenReturn(2);

        Assertions.assertThrows(RuntimeException.class, () -> basketService.deleteProduct(new DeleteProductRequest(1L), 1L));
    }

    @Test
    void whenChangeCountProductSuccess() {
        Mockito.when(basketRepository.existsBasketEntityByProductIdAndUserId(1L, 1L)).thenReturn(true);
        Mockito.when(basketRepository.updateCountByProductIdAndUserId(1L, 1L, 1L)).thenReturn(1);

        Assertions.assertEquals(new ChangeCountProductResponse(true), basketService.changeCountProduct(new ChangeCountProductRequest(1L, 1L), 1L));
    }

    @Test
    void whenChangeCountNotFoundFail() {
        Mockito.when(basketRepository.existsBasketEntityByProductIdAndUserId(1L, 1L)).thenReturn(false);

        Assertions.assertThrows(RuntimeException.class, () -> basketService.changeCountProduct(new ChangeCountProductRequest(1L, 1L), 1L));
    }

    @Test
    void whenChangeCountFail() {
        Mockito.when(basketRepository.existsBasketEntityByProductIdAndUserId(1L, 1L)).thenReturn(true);
        Mockito.when(basketRepository.updateCountByProductIdAndUserId(1L, 1L, 1L)).thenReturn(0);

        Assertions.assertThrows(RuntimeException.class, () -> basketService.changeCountProduct(new ChangeCountProductRequest(1L, 1L), 1L));
    }

    @Test
    void whenDeleteAllBasketProductSuccess() {
        Assertions.assertEquals(new DeleteAllProductResponse(true), basketService.deleteAllProduct(1L));
    }

    @Test
    void whenGetBasketSuccess() {
        List<BasketEntity> basketProducts = new ArrayList<>();
        basketProducts.add(new BasketEntity(1L, 1L, 1L));
        basketProducts.add(new BasketEntity(1L, 2L, 2L));
        Mockito.when(basketRepository.findByUserId(1L)).thenReturn(basketProducts);
        GetProductsInfoResponse productsInfoResponse = new GetProductsInfoResponse(List.of(
                new ProductInfo(1L, "image", "title", "description", "subtitle", 1L, "type", "measure", "unit", true),
                new ProductInfo(2L, "image2", "title2", "description2", "subtitle2", 2L, "type2", "measure2", "unit2", false)
        ));
        Mockito.when(integrationService.getProductInfoByIds(List.of(1L, 2L), "USER")).thenReturn(productsInfoResponse);
        GetBasketResponse expResponse = new GetBasketResponse(List.of(
                new Product(1L, "image", "title", 1L, null, 1L),
                new Product(2L, "image2", "title2", 2L, null, 2L)
        ), 5L);

        GetBasketResponse actResponse = basketService.getBasket(1L, "USER");
        Assertions.assertEquals(expResponse, actResponse);
    }

    @Test
    void whenGetBasketProductsNotFoundFail() {
        Mockito.when(basketRepository.findByUserId(1L)).thenReturn(null);

        Assertions.assertThrows(RuntimeException.class, () -> basketService.getBasket(1L, "USER"));
    }

    @Test
    void whenGetBasketProductsInfoNotCompleteFail() {
        List<BasketEntity> basketProducts = new ArrayList<>();
        basketProducts.add(new BasketEntity(1L, 1L, 1L));
        basketProducts.add(new BasketEntity(1L, 2L, 2L));
        Mockito.when(basketRepository.findByUserId(1L)).thenReturn(basketProducts);
        GetProductsInfoResponse productsInfoResponse = new GetProductsInfoResponse(List.of(
                new ProductInfo(1L, "image", "title", "description", "subtitle", 1L, "type", "measure", "unit", true)
        ));
        Mockito.when(integrationService.getProductInfoByIds(List.of(1L, 2L), "USER")).thenReturn(productsInfoResponse);

        Assertions.assertThrows(RuntimeException.class, () -> basketService.getBasket(1L, "USER"));
    }

    @Test
    void whenApplyPromocodeSuccess() {
        PromocodeEntity promocode = new PromocodeEntity("promo", 10L, true);
        Mockito.when(promocodeRepository.findByPromocode("promo")).thenReturn(promocode);
        List<BasketEntity> basketProducts = new ArrayList<>();
        basketProducts.add(new BasketEntity(1L, 1L, 1L));
        basketProducts.add(new BasketEntity(1L, 2L, 2L));
        Mockito.when(basketRepository.findByUserId(1L)).thenReturn(basketProducts);
        GetProductsInfoResponse productsInfoResponse = new GetProductsInfoResponse(List.of(
                new ProductInfo(1L, "image", "title", "description", "subtitle", 10L, "type", "measure", "unit", true),
                new ProductInfo(2L, "image2", "title2", "description2", "subtitle2", 20L, "type2", "measure2", "unit2", false)
        ));
        Mockito.when(integrationService.getProductInfoByIds(List.of(1L, 2L), "USER")).thenReturn(productsInfoResponse);
        ApplyPromocodeResponse expResponse = new ApplyPromocodeResponse(List.of(
                new Product(1L, "image", "title", 10L, 9L, 1L),
                new Product(2L, "image2", "title2", 20L, 18L, 2L)
        ), 50L, 45L);
        ApplyPromocodeRequest rq = new ApplyPromocodeRequest("promo");

        ApplyPromocodeResponse actResponse = basketService.applyPromocode(rq, 1L, "USER");
        Assertions.assertEquals(expResponse, actResponse);
    }

    @Test
    void whenApplyPromocodeNotFoundFail() {
        Mockito.when(promocodeRepository.findByPromocode("promo")).thenReturn(null);

        Assertions.assertThrows(RuntimeException.class, () -> basketService.applyPromocode(new ApplyPromocodeRequest("promo"), 1L, "USER"));
    }

    @Test
    void whenApplyPromocodeNotActiveFail() {
        Mockito.when(promocodeRepository.findByPromocode("promo")).thenReturn(new PromocodeEntity("promo", 10L, false));

        Assertions.assertThrows(RuntimeException.class, () -> basketService.applyPromocode(new ApplyPromocodeRequest("promo"), 1L, "USER"));
    }

    @Test
    void whenApplyPromocodeWithoutProductsFail() {
        PromocodeEntity promocode = new PromocodeEntity("promo", 10L, true);
        Mockito.when(promocodeRepository.findByPromocode("promo")).thenReturn(promocode);
        Mockito.when(basketRepository.findByUserId(1L)).thenReturn(null);

        Assertions.assertThrows(RuntimeException.class, () -> basketService.applyPromocode(new ApplyPromocodeRequest("promo"), 1L, "USER"));
    }

    @Test
    void whenApplyPromocodeInfoProductNotCompleteFail() {
        PromocodeEntity promocode = new PromocodeEntity("promo", 10L, true);
        Mockito.when(promocodeRepository.findByPromocode("promo")).thenReturn(promocode);
        List<BasketEntity> basketProducts = new ArrayList<>();
        basketProducts.add(new BasketEntity(1L, 1L, 1L));
        basketProducts.add(new BasketEntity(1L, 2L, 2L));
        Mockito.when(basketRepository.findByUserId(1L)).thenReturn(basketProducts);
        GetProductsInfoResponse productsInfoResponse = new GetProductsInfoResponse(List.of(
                new ProductInfo(1L, "image", "title", "description", "subtitle", 10L, "type", "measure", "unit", true)
        ));
        Mockito.when(integrationService.getProductInfoByIds(List.of(1L, 2L), "USER")).thenReturn(productsInfoResponse);

        Assertions.assertThrows(RuntimeException.class, () -> basketService.applyPromocode(new ApplyPromocodeRequest("promo"), 1L, "USER"));
    }
}
