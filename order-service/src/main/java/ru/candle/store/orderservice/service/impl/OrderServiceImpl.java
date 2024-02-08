package ru.candle.store.orderservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.candle.store.orderservice.dictionary.ExceptionCode;
import ru.candle.store.orderservice.dictionary.Status;
import ru.candle.store.orderservice.dto.Order;
import ru.candle.store.orderservice.dto.request.order.*;
import ru.candle.store.orderservice.dto.response.ProductInfo;
import ru.candle.store.orderservice.dto.response.integration.GetProductsInfoResponse;
import ru.candle.store.orderservice.dto.response.integration.GetUserInfoResponse;
import ru.candle.store.orderservice.dto.response.order.*;
import ru.candle.store.orderservice.entity.OrderEntity;
import ru.candle.store.orderservice.entity.ProductEntity;
import ru.candle.store.orderservice.entity.PromocodeEntity;
import ru.candle.store.orderservice.exception.OrderException;
import ru.candle.store.orderservice.repository.OrderRepository;
import ru.candle.store.orderservice.repository.PromocodeRepository;
import ru.candle.store.orderservice.service.IIntegrationService;
import ru.candle.store.orderservice.service.IOrderService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PromocodeRepository promocodeRepository;

    @Autowired
    private IIntegrationService integrationService;

    @Override
    public AddOrderResponse addOrder(AddOrderRequest rq, Long userId, String role) {
        try {
            return addOrderResponse(rq, userId, role);
        } catch (OrderException e) {
            log.error(e.getMessage());
            return AddOrderResponse.builder()
                    .success(false)
                    .errorCode(e.getE().getErrorCode())
                    .errorText(e.getE().getErrorText())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return AddOrderResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                    .build();
        }
    }

    @Override
    public GetOrderResponse getOrder(GetOrderRequest rq, Long userId, String role) {
        try {
            return getOrderResponse(rq, userId, role);
        } catch (OrderException e) {
            log.error(e.getMessage());
            return GetOrderResponse.builder()
                    .success(false)
                    .errorCode(e.getE().getErrorCode())
                    .errorText(e.getE().getErrorText())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return GetOrderResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                    .build();
        }
    }

    @Override
    public ChangeOrderStatusResponse changeOrderStatus(ChangeOrderStatusRequest rq) {
        try {
            return changeOrderStatusResponse(rq);
        } catch (OrderException e) {
            log.error(e.getMessage());
            return ChangeOrderStatusResponse.builder()
                    .success(false)
                    .errorCode(e.getE().getErrorCode())
                    .errorText(e.getE().getErrorText())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ChangeOrderStatusResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                    .build();
        }
    }

    @Override
    public GetAllOrdersByStatusResponse getAllOrdersByStatus(GetAllOrdersByStatusRequest rq) {
        try {
            return getAllOrdersByStatusResponse(rq);
        } catch (OrderException e) {
            log.error(e.getMessage());
            return GetAllOrdersByStatusResponse.builder()
                    .success(false)
                    .errorCode(e.getE().getErrorCode())
                    .errorText(e.getE().getErrorText())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return GetAllOrdersByStatusResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                    .build();
        }
    }

    @Override
    public GetOrderListResponse getOrderList(Long userId) {
        try {
            return getOrderListResponse(userId);
        } catch (OrderException e) {
            log.error(e.getMessage());
            return GetOrderListResponse.builder()
                    .success(false)
                    .errorCode(e.getE().getErrorCode())
                    .errorText(e.getE().getErrorText())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return GetOrderListResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                    .build();
        }
    }

    @Override
    public IsUserPurchasedProductResponse isUserPurchasedProduct(IsUserPurchasedProductRequest rq) {
        try {
            return isUserPurchasedProductResponse(rq);
        } catch (OrderException e) {
            return IsUserPurchasedProductResponse.builder()
                    .success(true)
                    .isPurchased(false)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return IsUserPurchasedProductResponse.builder()
                    .success(false)
                    .errorCode(ExceptionCode.UNKNOWN_EXCEPTION.getErrorCode())
                    .errorText(ExceptionCode.UNKNOWN_EXCEPTION.getErrorText())
                    .build();
        }
    }

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private AddOrderResponse addOrderResponse(AddOrderRequest rq, Long userId, String role) throws OrderException {
        Long promocodePercent = null;
        if (rq.getPromocode() != null) {
            PromocodeEntity promocodeEntity = promocodeRepository.findByPromocode(rq.getPromocode());
            if (promocodeEntity == null) {
                throw new OrderException(ExceptionCode.PROMOCODE_NOT_FOUND, "Промокод не найден");
            }
            if (promocodeEntity.getActual().equals(false)) {
                throw new OrderException(ExceptionCode.PROMOCODE_NOT_ACTUAL, "Промокод не действителен");
            }
            promocodePercent = promocodeEntity.getPercent();
        }
        List<Long> productIds = new ArrayList<>();
        rq.getProductsAndCounts().forEach(productAndCount -> productIds.add(productAndCount.getProductId()));
        GetProductsInfoResponse productsInfo = integrationService.getProductInfoByIds(productIds, role);
        if (productsInfo.getProductsInfo().isEmpty() || productsInfo.getProductsInfo().size() != productIds.size()) {
            throw new OrderException(ExceptionCode.GET_PRODUCTS_INFO_NOT_COMPLETE, "Получена информация не обо всех продуктах");
        }
        List<String> notActualProduct = productsInfo.getProductsInfo().stream()
                .filter(p -> !p.getActual())
                .map(ProductInfo::getTitle)
                .toList();
        if (!notActualProduct.isEmpty()) {
            throw new OrderException(ExceptionCode.PART_OF_PRODUCTS_NOT_ACTUAL, "Часть продуктов приобрести нельзя, удалите их из корзины: " + notActualProduct);
        }

        List<ProductEntity> productEntities = new ArrayList<>();
        Long totalPrice = 0L;
        Long totalPromoPrice = 0L;
        for (ProductInfo productInfo : productsInfo.getProductsInfo()) {
            Long count = rq.getProductsAndCounts().stream().filter(p ->
                    p.getProductId().equals(productInfo.getProductId())).findFirst().get().getCountId();
            ProductEntity productEntity = new ProductEntity(
                    productInfo.getProductId(),
                    productInfo.getImageId(),
                    productInfo.getTitle(),
                    productInfo.getPrice(),
                    null,
                    count
            );
            totalPrice += productInfo.getPrice() * count;
            if (promocodePercent != null) {
                productEntity.setPromoPrice((long) (productInfo.getPrice() * (1 - promocodePercent / 100.00)));
                totalPromoPrice += (long) (productInfo.getPrice() * (1 - promocodePercent / 100.00) * count);
            }
            productEntities.add(productEntity);
        }
        ;
        totalPromoPrice = totalPrice > 0L && totalPromoPrice == 0L ? null : totalPromoPrice;

        OrderEntity orderEntity = new OrderEntity(
                null,
                userId,
                LocalDateTime.now(ZoneId.of("UTC+5")).format(dateTimeFormatter),
                rq.getAddress(),
                rq.getPromocode(),
                totalPrice,
                totalPromoPrice,
                productEntities,
                Status.NEW
        );
        orderRepository.save(orderEntity);
        orderRepository.deleteAllByUserId(userId);
        return AddOrderResponse.builder().success(true).build();
    }

    private GetOrderResponse getOrderResponse(GetOrderRequest rq, Long userId, String role) throws OrderException {
        GetUserInfoResponse userInfo = integrationService.getUserInfo(userId, role);
        OrderEntity orderEntity = orderRepository.findByIdAndUserId(rq.getOrderId(), userId);
        if (orderEntity == null) {
            throw new OrderException(ExceptionCode.ORDER_NOT_FOUND, "Заказ не найден");
        }
        return GetOrderResponse.builder()
                .success(true)
                .firstName(userInfo.getFirstName())
                .lastName(userInfo.getLastName())
                .address(orderEntity.getAddress())
                .date(orderEntity.getDate())
                .promocode(orderEntity.getPromocode())
                .totalPrice(orderEntity.getTotalPrice())
                .totalPromoPrice(orderEntity.getTotalPromoPrice())
                .products(orderEntity.getDetails())
                .status(orderEntity.getStatus())
                .build();
    }

    private ChangeOrderStatusResponse changeOrderStatusResponse(ChangeOrderStatusRequest rq) throws OrderException {
        Optional<OrderEntity> orderEntity = orderRepository.findById(rq.getOrderId());
        if (orderEntity.isEmpty()) {
            throw new OrderException(ExceptionCode.ORDER_NOT_FOUND, "Заказ не найден");
        }
        orderEntity.get().setStatus(rq.getStatus());
        orderRepository.save(orderEntity.get());
        return ChangeOrderStatusResponse.builder().success(true).build();
    }

    GetAllOrdersByStatusResponse getAllOrdersByStatusResponse(GetAllOrdersByStatusRequest rq) throws OrderException {
        List<OrderEntity> orderEntities = orderRepository.findAllByStatus(rq.getStatus());
        if (orderEntities.isEmpty()) {
            throw new OrderException(ExceptionCode.ORDER_IN_SEARCH_STATUS_NOT_FOUND, "Заказов в искомом статусе нет");
        }
        List<Order> orders = new ArrayList<>();
        orderEntities.forEach(o -> {
            orders.add(new Order(
                    o.getId(),
                    o.getDate(),
                    o.getTotalPrice(),
                    o.getTotalPromoPrice(),
                    o.getStatus()
            ));
        });
        return GetAllOrdersByStatusResponse.builder()
                .success(true)
                .orders(orders)
                .build();
    }

    private GetOrderListResponse getOrderListResponse(Long userId) throws OrderException {
        List<OrderEntity> orderEntities = orderRepository.findAllByUserId(userId);
        if (orderEntities.isEmpty()) {
            throw new OrderException(ExceptionCode.USER_DONT_HAVE_ORDERS, "У пользователя нет заказов");
        }
        List<Order> orders = new ArrayList<>();
        orderEntities.forEach(o -> {
            orders.add(new Order(
                    o.getId(),
                    o.getDate(),
                    o.getTotalPrice(),
                    o.getTotalPromoPrice(),
                    o.getStatus()
            ));
        });
        return GetOrderListResponse.builder()
                .success(true)
                .orders(orders)
                .build();
    }


    private IsUserPurchasedProductResponse isUserPurchasedProductResponse(IsUserPurchasedProductRequest rq) throws OrderException {
        List<OrderEntity> orderEntities = orderRepository.findAllByUserId(rq.getUserId());
        if (orderEntities.isEmpty()) {
            throw new OrderException(ExceptionCode.USER_DONT_HAVE_ORDERS, "У пользователя нет заказов");
        }
        for (OrderEntity order : orderEntities) {
            for (ProductEntity product: order.getDetails()) {
                if (product.getProductId().equals(rq.getProductId())) {
                    return IsUserPurchasedProductResponse.builder()
                            .success(true)
                            .isPurchased(true)
                            .build();
                }
            }
        }
        return IsUserPurchasedProductResponse.builder()
                .success(true)
                .isPurchased(false)
                .build();
    }
}
