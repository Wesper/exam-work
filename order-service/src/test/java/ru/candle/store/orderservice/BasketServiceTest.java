package ru.candle.store.orderservice;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.candle.store.orderservice.dictionary.ExceptionCode;
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
import ru.candle.store.orderservice.exception.OrderException;
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
    void whenAddProductSuccess() throws OrderException {
        AddProductRequest rq = new AddProductRequest(1L, 1L);
        List<ProductInfo> productsInfo = List.of(ProductInfo.builder()
                .productId(1L)
                .imageId("image")
                .title("title")
                .description("decription")
                .subtitle("subtitle")
                .price(1L)
                .type("type")
                .measure("measure")
                .unitMeasure("unit")
                .actual(true)
                .build());
        GetProductsInfoResponse productInfoResponse = GetProductsInfoResponse.builder()
                .success(true)
                .productsInfo(productsInfo)
                .build();
        Mockito.when(integrationService.getProductInfoByIds(List.of(1L), "USER")).thenReturn(productInfoResponse);
        BasketEntity entity = new BasketEntity(1L, 1L, 1L);
        Mockito.when(basketRepository.save(entity)).thenReturn(entity);

        AddProductResponse rs = basketService.addProduct(rq, 1L, "USER");
        Assertions.assertEquals(AddProductResponse.builder().success(true).build(), rs);
    }

    @Test
    void whenAddProductFail() throws OrderException {
        AddProductRequest rq = new AddProductRequest(1L, 1L);
        Mockito.when(integrationService.getProductInfoByIds(List.of(1L), "USER"))
                .thenThrow(new OrderException(ExceptionCode.GET_PRODUCTS_INFO_IS_NULL, ""));

        AddProductResponse rs = basketService.addProduct(rq, 1L, "USER");
        Assertions.assertAll(
                () -> Assertions.assertFalse(rs.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.GET_PRODUCTS_INFO_IS_NULL.getErrorCode(), rs.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.GET_PRODUCTS_INFO_IS_NULL.getErrorText(), rs.getErrorText())
        );
    }

    @Test
    void whenDeleteProductSuccess() {
        Mockito.when(basketRepository.existsBasketEntityByProductIdAndUserId(1L, 1L)).thenReturn(true);
        Mockito.when(basketRepository.deleteByProductIdAndUserId(1L, 1L)).thenReturn(1);

        Assertions.assertEquals(DeleteProductResponse.builder().success(true).build(),
                basketService.deleteProduct(new DeleteProductRequest(1L), 1L));
    }

    @Test
    void whenDeleteProductNotFoundFail() {
        Mockito.when(basketRepository.existsBasketEntityByProductIdAndUserId(1L, 1L)).thenReturn(false);

        DeleteProductResponse rs = basketService.deleteProduct(new DeleteProductRequest(1L), 1L);
        Assertions.assertAll(
                () -> Assertions.assertFalse(rs.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.PRODUCT_NOT_FOUND.getErrorCode(), rs.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.PRODUCT_NOT_FOUND.getErrorText(), rs.getErrorText())
        );
    }

    @Test
    void whenDeleteProductFail() {
        Mockito.when(basketRepository.existsBasketEntityByProductIdAndUserId(1L, 1L)).thenReturn(true);
        Mockito.when(basketRepository.deleteByProductIdAndUserId(1L, 1L)).thenReturn(0);

        DeleteProductResponse rs = basketService.deleteProduct(new DeleteProductRequest(1L), 1L);
        Assertions.assertAll(
                () -> Assertions.assertFalse(rs.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.DELETE_PRODUCT_ERROR.getErrorCode(), rs.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.DELETE_PRODUCT_ERROR.getErrorText(), rs.getErrorText())
        );
    }

    @Test
    void whenChangeCountProductSuccess() {
        Mockito.when(basketRepository.existsBasketEntityByProductIdAndUserId(1L, 1L)).thenReturn(true);
        Mockito.when(basketRepository.updateCountByProductIdAndUserId(1L, 1L, 1L)).thenReturn(1);

        Assertions.assertEquals(ChangeCountProductResponse.builder().success(true).build(),
                basketService.changeCountProduct(new ChangeCountProductRequest(1L, 1L), 1L));
    }

    @Test
    void whenChangeCountNotFoundFail() {
        Mockito.when(basketRepository.existsBasketEntityByProductIdAndUserId(1L, 1L)).thenReturn(false);

        ChangeCountProductResponse rs = basketService.changeCountProduct(new ChangeCountProductRequest(1L, 1L), 1L);
        Assertions.assertAll(
                () -> Assertions.assertFalse(rs.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.PRODUCT_NOT_FOUND.getErrorCode(), rs.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.PRODUCT_NOT_FOUND.getErrorText(), rs.getErrorText())
        );
    }

    @Test
    void whenChangeCountFail() {
        Mockito.when(basketRepository.existsBasketEntityByProductIdAndUserId(1L, 1L)).thenReturn(true);
        Mockito.when(basketRepository.updateCountByProductIdAndUserId(1L, 1L, 1L)).thenReturn(0);

        ChangeCountProductResponse rs = basketService.changeCountProduct(new ChangeCountProductRequest(1L, 1L), 1L);
        Assertions.assertAll(
                () -> Assertions.assertFalse(rs.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode(), rs.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText(), rs.getErrorText())
        );
    }

    @Test
    void whenDeleteAllBasketProductSuccess() {
        Assertions.assertEquals(DeleteAllProductResponse.builder().success(true).build(),
                basketService.deleteAllProduct(1L));
    }

    @Test
    void whenGetBasketSuccess() throws OrderException {
        List<BasketEntity> basketProducts = List.of(
                new BasketEntity(1L, 1L, 1L),
                new BasketEntity(1L, 2L, 2L)
        );
        Mockito.when(basketRepository.findByUserId(1L)).thenReturn(basketProducts);
        List<ProductInfo> productsInfo = List.of(
                new ProductInfo(1L, "image", "title", "description", "subtitle",
                        1L, "type", "measure", "unit", true),
                new ProductInfo(2L, "image2", "title2", "description2", "subtitle2",
                        2L, "type2", "measure2", "unit2", false)
        );
        GetProductsInfoResponse productsInfoResponse = GetProductsInfoResponse.builder()
                .success(true)
                .productsInfo(productsInfo)
                .build();
        Mockito.when(integrationService.getProductInfoByIds(List.of(1L, 2L), "USER")).thenReturn(productsInfoResponse);
        List<Product> products = List.of(
                new Product(1L, "image", "title", 1L, null, 1L),
                new Product(2L, "image2", "title2", 2L, null, 2L)
        );
        GetBasketResponse expResponse = GetBasketResponse.builder().success(true).products(products).totalPrice(5L).build();

        GetBasketResponse actResponse = basketService.getBasket(1L, "USER");
        Assertions.assertEquals(expResponse, actResponse);
    }

    @Test
    void whenGetBasketProductsNotFoundFail() {
        Mockito.when(basketRepository.findByUserId(1L)).thenReturn(new ArrayList<>());

        GetBasketResponse rs = basketService.getBasket(1L, "USER");
        Assertions.assertAll(
                () -> Assertions.assertFalse(rs.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.PRODUCT_NOT_FOUND.getErrorCode(), rs.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.PRODUCT_NOT_FOUND.getErrorText(), rs.getErrorText())
        );
    }

    @Test
    void whenGetBasketProductsInfoNotCompleteFail() throws OrderException {
        List<BasketEntity> basketProducts = List.of(
                new BasketEntity(1L, 1L, 1L),
                new BasketEntity(1L, 2L, 2L)
        );
        Mockito.when(basketRepository.findByUserId(1L)).thenReturn(basketProducts);
        List<ProductInfo> productsInfo = List.of(
                new ProductInfo(1L, "image", "title", "description", "subtitle",
                        1L, "type", "measure", "unit", true)
        );
        GetProductsInfoResponse productsInfoResponse = GetProductsInfoResponse.builder().success(true).productsInfo(productsInfo).build();
        Mockito.when(integrationService.getProductInfoByIds(List.of(1L, 2L), "USER")).thenReturn(productsInfoResponse);

        GetBasketResponse rs = basketService.getBasket(1L, "USER");
        Assertions.assertAll(
                () -> Assertions.assertFalse(rs.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.GET_PRODUCTS_INFO_NOT_COMPLETE.getErrorCode(), rs.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.GET_PRODUCTS_INFO_NOT_COMPLETE.getErrorText(), rs.getErrorText())
        );
    }

    @Test
    void whenApplyPromocodeSuccess() throws OrderException {
        PromocodeEntity promocode = new PromocodeEntity("promo", 10L, true);
        Mockito.when(promocodeRepository.findByPromocode("promo")).thenReturn(promocode);
        List<BasketEntity> basketProducts = List.of(
                new BasketEntity(1L, 1L, 1L),
                new BasketEntity(1L, 2L, 2L)
        );
        Mockito.when(basketRepository.findByUserId(1L)).thenReturn(basketProducts);
        List<ProductInfo> productsInfo = List.of(
                new ProductInfo(1L, "image", "title", "description", "subtitle",
                        10L, "type", "measure", "unit", true),
                new ProductInfo(2L, "image2", "title2", "description2", "subtitle2",
                        20L, "type2", "measure2", "unit2", false)
        );
        GetProductsInfoResponse productsInfoResponse = GetProductsInfoResponse.builder().success(true).productsInfo(productsInfo).build();
        Mockito.when(integrationService.getProductInfoByIds(List.of(1L, 2L), "USER")).thenReturn(productsInfoResponse);
        List<Product> products = List.of(
                new Product(1L, "image", "title", 10L, 9L, 1L),
                new Product(2L, "image2", "title2", 20L, 18L, 2L)
        );
        ApplyPromocodeResponse expResponse = ApplyPromocodeResponse.builder()
                .success(true)
                .products(products)
                .totalPrice(50L)
                .totalPricePromo(45L)
                .build();
        ApplyPromocodeRequest rq = new ApplyPromocodeRequest("promo");

        ApplyPromocodeResponse actResponse = basketService.applyPromocode(rq, 1L, "USER");
        Assertions.assertEquals(expResponse, actResponse);
    }

    @Test
    void whenApplyPromocodeNotFoundFail() {
        Mockito.when(promocodeRepository.findByPromocode("promo")).thenReturn(null);

        ApplyPromocodeResponse rs = basketService.applyPromocode(new ApplyPromocodeRequest("promo"), 1L, "USER");
        Assertions.assertAll(
                () -> Assertions.assertFalse(rs.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.PROMOCODE_NOT_FOUND.getErrorCode(), rs.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.PROMOCODE_NOT_FOUND.getErrorText(), rs.getErrorText())
        );
    }

    @Test
    void whenApplyPromocodeNotActiveFail() {
        Mockito.when(promocodeRepository.findByPromocode("promo")).thenReturn(new PromocodeEntity("promo", 10L, false));

        ApplyPromocodeResponse rs = basketService.applyPromocode(new ApplyPromocodeRequest("promo"), 1L, "USER");
        Assertions.assertAll(
                () -> Assertions.assertFalse(rs.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.PROMOCODE_NOT_ACTUAL.getErrorCode(), rs.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.PROMOCODE_NOT_ACTUAL.getErrorText(), rs.getErrorText())
        );
    }

    @Test
    void whenApplyPromocodeWithoutProductsFail() {
        PromocodeEntity promocode = new PromocodeEntity("promo", 10L, true);
        Mockito.when(promocodeRepository.findByPromocode("promo")).thenReturn(promocode);
        Mockito.when(basketRepository.findByUserId(1L)).thenReturn(new ArrayList<>());

        ApplyPromocodeResponse rs = basketService.applyPromocode(new ApplyPromocodeRequest("promo"), 1L, "USER");
        Assertions.assertAll(
                () -> Assertions.assertFalse(rs.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.USER_BASKET_IS_EMPTY.getErrorCode(), rs.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.USER_BASKET_IS_EMPTY.getErrorText(), rs.getErrorText())
        );
    }

    @Test
    void whenApplyPromocodeInfoProductNotCompleteFail() throws OrderException {
        PromocodeEntity promocode = new PromocodeEntity("promo", 10L, true);
        Mockito.when(promocodeRepository.findByPromocode("promo")).thenReturn(promocode);
        List<BasketEntity> basketProducts = new ArrayList<>();
        basketProducts.add(new BasketEntity(1L, 1L, 1L));
        basketProducts.add(new BasketEntity(1L, 2L, 2L));
        Mockito.when(basketRepository.findByUserId(1L)).thenReturn(basketProducts);
        List<ProductInfo> productsInfos = List.of(
                new ProductInfo(1L, "image", "title", "description", "subtitle",
                        10L, "type", "measure", "unit", true)
        );
        GetProductsInfoResponse productsInfoResponse = GetProductsInfoResponse.builder().success(true).productsInfo(productsInfos).build();
        Mockito.when(integrationService.getProductInfoByIds(List.of(1L, 2L), "USER")).thenReturn(productsInfoResponse);

        ApplyPromocodeResponse rs = basketService.applyPromocode(new ApplyPromocodeRequest("promo"), 1L, "USER");
        Assertions.assertAll(
                () -> Assertions.assertFalse(rs.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.GET_PRODUCTS_INFO_NOT_COMPLETE.getErrorCode(), rs.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.GET_PRODUCTS_INFO_NOT_COMPLETE.getErrorText(), rs.getErrorText())
        );
    }
}
