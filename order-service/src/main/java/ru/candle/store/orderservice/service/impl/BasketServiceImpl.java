package ru.candle.store.orderservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import ru.candle.store.orderservice.service.IBasketService;
import ru.candle.store.orderservice.service.IIntegrationService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class BasketServiceImpl implements IBasketService {

    @Autowired
    private IIntegrationService integrationService;

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private PromocodeRepository promocodeRepository;

    @Override
    public AddProductResponse addProduct(AddProductRequest rq, Long userId, String role) {
        try {
            return addProductResponse(rq, userId, role);
        } catch (OrderException e) {
            log.error(e.getMessage());
            return AddProductResponse.builder()
                    .success(false)
                    .errorCode(e.getE().getErrorCode())
                    .errorText(e.getE().getErrorText())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return AddProductResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .build();
        }
    }

    @Override
    public DeleteProductResponse deleteProduct(DeleteProductRequest rq, Long userId) {
        try {
            return deleteProductResponse(rq, userId);
        } catch (OrderException e) {
            log.error(e.getMessage());
            return DeleteProductResponse.builder()
                    .success(false)
                    .errorCode(e.getE().getErrorCode())
                    .errorText(e.getE().getErrorText())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return DeleteProductResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .build();
        }
    }

    @Override
    public ChangeCountProductResponse changeCountProduct(ChangeCountProductRequest rq, Long userId) {
        try {
            return changeCountProductResponse(rq, userId);
        } catch (OrderException e) {
            log.error(e.getMessage());
            return ChangeCountProductResponse.builder()
                    .success(false)
                    .errorCode(e.getE().getErrorCode())
                    .errorText(e.getE().getErrorText())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ChangeCountProductResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .build();
        }
    }

    @Override
    public DeleteAllProductResponse deleteAllProduct(Long userId) {
        try {
            return deleteAllProductResponse(userId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DeleteAllProductResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .build();
        }
    }

    @Override
    public GetBasketResponse getBasket(Long userId, String role) {
        try {
            return getBasketResponse(userId, role);
        } catch (OrderException e) {
            log.error(e.getMessage());
            return GetBasketResponse.builder()
                    .success(false)
                    .errorCode(e.getE().getErrorCode())
                    .errorText(e.getE().getErrorText())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return GetBasketResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .build();
        }
    }

    @Override
    public ApplyPromocodeResponse applyPromocode(ApplyPromocodeRequest rq, Long userId, String role) {
        try {
            return applyPromocodeResponse(rq, userId, role);
        } catch (OrderException e) {
            log.error(e.getMessage());
            return ApplyPromocodeResponse.builder()
                    .success(false)
                    .errorCode(e.getE().getErrorCode())
                    .errorText(e.getE().getErrorText())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApplyPromocodeResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .build();
        }
    }

    private AddProductResponse addProductResponse(AddProductRequest rq, Long userId, String role) throws OrderException {
        GetProductsInfoResponse productInfo = integrationService.getProductInfoByIds(List.of(rq.getProductId()), role);
        BasketEntity entity = new BasketEntity(
                userId,
                productInfo.getProductsInfo().get(0).getProductId(),
                rq.getCount()
        );
        basketRepository.save(entity);
        return AddProductResponse.builder().success(true).build();
    }

    private DeleteProductResponse deleteProductResponse(DeleteProductRequest rq, Long userId) throws OrderException {
        boolean productIsExist = basketRepository.existsBasketEntityByProductIdAndUserId(rq.getProductId(), userId);
        if (!productIsExist) {
            throw new OrderException(ExceptionCode.PRODUCT_NOT_FOUND, "У пользователя нет запрашиваемого продукта");
        }
        int countDeleteRows = basketRepository.deleteByProductIdAndUserId(rq.getProductId(), userId);
        if (countDeleteRows != 1) {
            throw new OrderException(ExceptionCode.DELETE_PRODUCT_ERROR, "Произошла ошибка при удалении продукта из корзины, количество затронутых строк " + countDeleteRows);
        }
        return DeleteProductResponse.builder().success(true).build();
    }

    private ChangeCountProductResponse changeCountProductResponse(ChangeCountProductRequest rq, Long userId) throws OrderException {
        boolean productIsExist = basketRepository.existsBasketEntityByProductIdAndUserId(rq.getProductId(), userId);
        if (!productIsExist) {
            throw new OrderException(ExceptionCode.PRODUCT_NOT_FOUND, "У пользователя нет запрашиваемого продукта");
        }
        int countUpdatedRows = basketRepository.updateCountByProductIdAndUserId(rq.getProductId(), userId, rq.getCount());
        if (countUpdatedRows != 1) {
            throw new OrderException(ExceptionCode.UNKNOWN_EXCEPTION, "Произошла ошибка при обновлении количества позиций продукта, количество затронутых строк " + countUpdatedRows);
        }
        return ChangeCountProductResponse.builder().success(true).build();
    }

    private DeleteAllProductResponse deleteAllProductResponse(Long userId) {
        basketRepository.deleteByUserId(userId);
        return DeleteAllProductResponse.builder().success(true).build();
    }

    private GetBasketResponse getBasketResponse(Long userId, String role) throws OrderException {
        List<BasketEntity> basketEntities = basketRepository.findByUserId(userId);
        if (basketEntities.isEmpty()) {
            throw new OrderException(ExceptionCode.PRODUCT_NOT_FOUND, "У пользователя нет продуктов в корзине");
        }
        List<Long> productIds = new ArrayList<>();
        basketEntities.forEach(basketEntity -> {
            productIds.add(basketEntity.getProductId());
        });
        GetProductsInfoResponse productsInfo = integrationService.getProductInfoByIds(productIds, role);
        if (productsInfo.getProductsInfo().isEmpty() || productsInfo.getProductsInfo().size() != productIds.size()) {
            throw new OrderException(ExceptionCode.GET_PRODUCTS_INFO_NOT_COMPLETE, "Получена не вся информация о продуктах");
        }
        List<Product> productsResponse = new ArrayList<>();
        AtomicReference<Long> totalPrice = new AtomicReference<>(0L);
        basketEntities.forEach(basketEntity -> {
            ProductInfo productInfo = productsInfo.getProductsInfo().stream().filter(p ->
                    basketEntity.getProductId().equals(p.getProductId())).findFirst().get();
            productsResponse.add(new Product(
                    basketEntity.getProductId(),
                    productInfo.getImageId(),
                    productInfo.getTitle(),
                    productInfo.getPrice(),
                    null,
                    basketEntity.getCount()
            ));
            totalPrice.updateAndGet(v -> v + productInfo.getPrice() * basketEntity.getCount());
        });
        return GetBasketResponse.builder()
                .success(true)
                .products(productsResponse)
                .totalPrice(totalPrice.get())
                .build();
    }

    private ApplyPromocodeResponse applyPromocodeResponse(ApplyPromocodeRequest rq, Long userId, String role) throws OrderException {
        PromocodeEntity promocodeEntity = promocodeRepository.findByPromocode(rq.getPromocode());
        if (promocodeEntity == null) {
            throw new OrderException(ExceptionCode.PROMOCODE_NOT_FOUND, "Промокода не существует");
        }
        if (!promocodeEntity.getActual()) {
            throw new OrderException(ExceptionCode.PROMOCODE_NOT_ACTUAL, "Промокод не активный");
        }
        List<BasketEntity> basketEntities = basketRepository.findByUserId(userId);
        if (basketEntities.isEmpty()) {
            throw new OrderException(ExceptionCode.USER_BASKET_IS_EMPTY, "У пользователя нет продуктов в корзине");
        }
        List<Long> productIds = new ArrayList<>();
        basketEntities.forEach(basketEntity -> {
            productIds.add(basketEntity.getProductId());
        });
        GetProductsInfoResponse productsInfo = integrationService.getProductInfoByIds(productIds, role);
        if (productsInfo.getProductsInfo().isEmpty() || productsInfo.getProductsInfo().size() != productIds.size()) {
            throw new OrderException(ExceptionCode.GET_PRODUCTS_INFO_NOT_COMPLETE, "Получена не вся информация о продуктах");
        }
        List<Product> productsResponse = new ArrayList<>();
        AtomicReference<Long> totalPrice = new AtomicReference<>(0L);
        AtomicReference<Long> totalPromoPrice = new AtomicReference<>(0L);
        basketEntities.forEach(basketEntity -> {
            ProductInfo productInfo = productsInfo.getProductsInfo().stream().filter(p ->
                    basketEntity.getProductId().equals(p.getProductId())).findFirst().get();
            productsResponse.add(new Product(
                    basketEntity.getProductId(),
                    productInfo.getImageId(),
                    productInfo.getTitle(),
                    productInfo.getPrice(),
                    (long) (productInfo.getPrice() * (1 - promocodeEntity.getPercent() / 100.00)),
                    basketEntity.getCount()
            ));
            totalPrice.updateAndGet(v -> v + productInfo.getPrice() * basketEntity.getCount());
            totalPromoPrice.updateAndGet(v -> (long) (v + (productInfo.getPrice() * (1 - promocodeEntity.getPercent() / 100.00)) * basketEntity.getCount()));
        });
        return ApplyPromocodeResponse.builder()
                .success(true)
                .products(productsResponse)
                .totalPrice(totalPrice.get())
                .totalPricePromo(totalPromoPrice.get())
                .build();
    }

}
