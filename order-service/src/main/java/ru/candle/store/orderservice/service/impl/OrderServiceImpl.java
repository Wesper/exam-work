package ru.candle.store.orderservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.candle.store.orderservice.dictionary.Status;
import ru.candle.store.orderservice.dto.Order;
import ru.candle.store.orderservice.dto.request.order.AddOrderRequest;
import ru.candle.store.orderservice.dto.request.order.ChangeOrderStatusRequest;
import ru.candle.store.orderservice.dto.request.order.GetAllOrdersByStatusRequest;
import ru.candle.store.orderservice.dto.request.order.GetOrderRequest;
import ru.candle.store.orderservice.dto.response.integration.GetProductsInfoResponse;
import ru.candle.store.orderservice.dto.response.integration.GetUserInfoResponse;
import ru.candle.store.orderservice.dto.response.order.*;
import ru.candle.store.orderservice.entity.OrderEntity;
import ru.candle.store.orderservice.entity.ProductEntity;
import ru.candle.store.orderservice.entity.PromocodeEntity;
import ru.candle.store.orderservice.repository.BasketRepository;
import ru.candle.store.orderservice.repository.OrderRepository;
import ru.candle.store.orderservice.repository.PromocodeRepository;
import ru.candle.store.orderservice.service.IIntegrationService;
import ru.candle.store.orderservice.service.IOrderService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PromocodeRepository promocodeRepository;

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private IIntegrationService integrationService;

    @Override
    public AddOrderResponse addOrder(AddOrderRequest rq, Long userId, String role) {
        return addOrderResponse(rq, userId, role);
    }

    @Override
    public GetOrderResponse getOrder(GetOrderRequest rq, Long userId, String role) {
        return getOrderResponse(rq, userId, role);
    }

    @Override
    public ChangeOrderStatusResponse changeOrderStatus(ChangeOrderStatusRequest rq) {
        return changeOrderStatusResponse(rq);
    }

    @Override
    public GetAllOrdersByStatusResponse getAllOrdersByStatus(GetAllOrdersByStatusRequest rq) {
        return getAllOrdersByStatusResponse(rq);
    }

    @Override
    public GetOrderListResponse getOrderList(Long userId) {
        return getOrderListResponse(userId);
    }

    private AddOrderResponse addOrderResponse(AddOrderRequest rq, Long userId, String role) {
        PromocodeEntity promocodeEntity = promocodeRepository.findByPromocode(rq.getPromocode());
        if (promocodeEntity == null) {
            throw new RuntimeException("Промокод не найден");
        }
        List<Long> productIds = new ArrayList<>();
        rq.getProductsAndCounts().forEach(productAndCount -> productIds.add(productAndCount.getProductId()));
        GetProductsInfoResponse productsInfo = integrationService.getProductInfoByIds(productIds, role);
        if (productsInfo.getProductsInfo().isEmpty() || productsInfo.getProductsInfo().size() != productIds.size()) {
            throw new RuntimeException("Получена информация не обо всех продуктах");
        }

        List<ProductEntity> productEntities = new ArrayList<>();
        AtomicReference<Long> totalPrice = new AtomicReference<>(0L);
        AtomicReference<Long> totalPromoPrice = new AtomicReference<>(0L);
        productsInfo.getProductsInfo().forEach(productInfo -> {
            Long count = rq.getProductsAndCounts().stream().filter(p ->
                    p.getProductId().equals(productInfo.getProductId())).findFirst().get().getCountId();
            productEntities.add(new ProductEntity(
                    productInfo.getProductId(),
                    productInfo.getImageId(),
                    productInfo.getTitle(),
                    productInfo.getPrice(),
                    productInfo.getPrice() * promocodeEntity.getPercent() / 100,
                    count
            ));
            totalPrice.updateAndGet(v -> v + productInfo.getPrice() * count);
            totalPromoPrice.updateAndGet(v -> v + (productInfo.getPrice() * promocodeEntity.getPercent() / 100) * count);
        });

        OrderEntity orderEntity = new OrderEntity(
                userId,
                LocalDateTime.now(ZoneId.of("Ekaterinburg")),
                rq.getAddress(),
                rq.getPromocode(),
                totalPrice.get(),
                totalPromoPrice.get(),
                productEntities,
                Status.NEW
        );
        orderRepository.save(orderEntity);
        orderRepository.deleteAllByUserId(userId);
        return new AddOrderResponse(true);
    }

    private GetOrderResponse getOrderResponse(GetOrderRequest rq, Long userId, String role) {
        GetUserInfoResponse userInfo = integrationService.getUserInfo(userId, role);
        OrderEntity orderEntity = orderRepository.findByIdAndUserId(rq.getOrderId(), userId);
        if (orderEntity == null) {
            throw new RuntimeException("Заказ не найден");
        }
        return new GetOrderResponse(
                userInfo.getFirstName(),
                userInfo.getLastName(),
                orderEntity.getAddress(),
                orderEntity.getDate(),
                orderEntity.getPromocode(),
                orderEntity.getTotalPrice(),
                orderEntity.getTotal_promo_price(),
                orderEntity.getDetails(),
                orderEntity.getStatus()
        );
    }

    private ChangeOrderStatusResponse changeOrderStatusResponse(ChangeOrderStatusRequest rq) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(rq.getOrderId());
        if (orderEntity.isEmpty()) {
            throw new RuntimeException("Заказ не найден");
        }
        orderEntity.get().setStatus(rq.getStatus());
        orderRepository.save(orderEntity.get());
        return new ChangeOrderStatusResponse(true);
    }

    GetAllOrdersByStatusResponse getAllOrdersByStatusResponse(GetAllOrdersByStatusRequest rq) {
        List<OrderEntity> orderEntities = orderRepository.findAllByStatus(rq.getStatus().name());
        if (orderEntities.isEmpty()) {
            throw new RuntimeException("Заказов в искомом статусе нет");
        }
        List<Order> orders = new ArrayList<>();
        orderEntities.forEach(o -> {
            orders.add(new Order(
                    o.getId(),
                    o.getDate(),
                    o.getTotalPrice(),
                    o.getTotal_promo_price(),
                    o.getStatus()
            ));
        });
        return new GetAllOrdersByStatusResponse(orders);
    }

    private GetOrderListResponse getOrderListResponse(Long userId) {
        List<OrderEntity> orderEntities = orderRepository.findAllByUserId(userId);
        if (orderEntities.isEmpty()) {
            throw new RuntimeException("У пользователя нет заказов");
        }
        List<Order> orders = new ArrayList<>();
        orderEntities.forEach(o -> {
            orders.add(new Order(
                    o.getId(),
                    o.getDate(),
                    o.getTotalPrice(),
                    o.getTotal_promo_price(),
                    o.getStatus()
            ));
        });
        return new GetOrderListResponse(orders);
    }

}
