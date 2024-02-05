package ru.candle.store.orderservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import ru.candle.store.orderservice.service.IBasketService;
import ru.candle.store.orderservice.service.IIntegrationService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
        return addProductResponse(rq, userId, role);
    }

    @Override
    public DeleteProductResponse deleteProduct(DeleteProductRequest rq, Long userId) {
        return deleteProductResponse(rq, userId);
    }

    @Override
    public ChangeCountProductResponse changeCountProduct(ChangeCountProductRequest rq, Long userId) {
        return changeCountProductResponse(rq, userId);
    }

    @Override
    public DeleteAllProductResponse deleteAllProduct(Long userId) {
        return deleteAllProductResponse(userId);
    }

    @Override
    public GetBasketResponse getBasket(Long userId, String role) {
        return getBasketResponse(userId, role);
    }

    @Override
    public ApplyPromocodeResponse applyPromocode(ApplyPromocodeRequest rq, Long userId, String role) {
        return applyPromocodeResponse(rq, userId, role);
    }

    private AddProductResponse addProductResponse(AddProductRequest rq, Long userId, String role) {
        GetProductsInfoResponse productInfo = integrationService.getProductInfoByIds(List.of(rq.getProductId()), role);
        BasketEntity entity = new BasketEntity(
                userId,
                productInfo.getProductsInfo().get(0).getProductId(),
                rq.getCount()
        );
        basketRepository.save(entity);
        return new AddProductResponse(true);
    }

    private DeleteProductResponse deleteProductResponse(DeleteProductRequest rq, Long userId) {
        boolean productIsExist = basketRepository.existByProductIdAndUserId(rq.getProductId(), userId);
        if (!productIsExist) {
            throw new RuntimeException("У пользователя нет запрашиваемого продукта");
        }
        int countDeleteRows = basketRepository.deleteByProductIdAndUserId(rq.getProductId(), userId);
        if (countDeleteRows != 1) {
            throw new RuntimeException("Произошла ошибка при удалении продукта из корзины, количество затронутых строк " + countDeleteRows);
        }
        return new DeleteProductResponse(true);
    }

    private ChangeCountProductResponse changeCountProductResponse(ChangeCountProductRequest rq, Long userId) {
        boolean productIsExist = basketRepository.existByProductIdAndUserId(rq.getProductId(), userId);
        if (!productIsExist) {
            throw new RuntimeException("У пользователя нет запрашиваемого продукта");
        }
        int countUpdatedRows = basketRepository.updateCountByProductIdAndUserId(rq.getProductId(), userId, rq.getCount());
        if (countUpdatedRows != 1) {
            throw new RuntimeException("Произошла ошибка при обновлении количества позиций продукта, количество затронутых строк " + countUpdatedRows);
        }
        return new ChangeCountProductResponse(true);
    }

    private DeleteAllProductResponse deleteAllProductResponse(Long userId) {
        basketRepository.deleteByUserId(userId);
        return new DeleteAllProductResponse(true);
    }

    private GetBasketResponse getBasketResponse(Long userId, String role) {
        List<BasketEntity> basketEntities = basketRepository.findByUserId(userId);
        if (basketEntities.isEmpty()) {
            throw new RuntimeException("У пользователя нет продуктов в корзине");
        }
        List<Long> productIds = new ArrayList<>();
        basketEntities.forEach(basketEntity -> {
            productIds.add(basketEntity.getProductId());
        });
        GetProductsInfoResponse productsInfo = integrationService.getProductInfoByIds(productIds, role);
        if (productsInfo.getProductsInfo().isEmpty() || productsInfo.getProductsInfo().size() != productIds.size()) {
            throw new RuntimeException("Получена не вся информация о продуктах");
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
        return new GetBasketResponse(productsResponse, totalPrice.get());
    }

    private ApplyPromocodeResponse applyPromocodeResponse(ApplyPromocodeRequest rq, Long userId, String role) {
        PromocodeEntity promocodeEntity = promocodeRepository.findByPromocode(rq.getPromocode());
        if (promocodeEntity == null) {
            throw new RuntimeException("Промокода не существует");
        }
        List<BasketEntity> basketEntities = basketRepository.findByUserId(userId);
        if (basketEntities.isEmpty()) {
            throw new RuntimeException("У пользователя нет продуктов в корзине");
        }
        List<Long> productIds = new ArrayList<>();
        basketEntities.forEach(basketEntity -> {
            productIds.add(basketEntity.getProductId());
        });
        GetProductsInfoResponse productsInfo = integrationService.getProductInfoByIds(productIds, role);
        if (productsInfo.getProductsInfo().isEmpty() || productsInfo.getProductsInfo().size() != productIds.size()) {
            throw new RuntimeException("Получена не вся информация о продуктах");
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
                    productInfo.getPrice() * promocodeEntity.getPercent() / 100,
                    basketEntity.getCount()
            ));
            totalPrice.updateAndGet(v -> v + productInfo.getPrice() * basketEntity.getCount());
            totalPromoPrice.updateAndGet(v -> v + (productInfo.getPrice() * promocodeEntity.getPercent() / 100) * basketEntity.getCount());
        });
        return new ApplyPromocodeResponse(productsResponse, totalPrice.get(), totalPromoPrice.get());
    }
}
