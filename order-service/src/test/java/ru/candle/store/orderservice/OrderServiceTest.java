package ru.candle.store.orderservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.candle.store.orderservice.dictionary.Status;
import ru.candle.store.orderservice.dto.Order;
import ru.candle.store.orderservice.dto.request.ProductAndCount;
import ru.candle.store.orderservice.dto.request.order.AddOrderRequest;
import ru.candle.store.orderservice.dto.request.order.ChangeOrderStatusRequest;
import ru.candle.store.orderservice.dto.request.order.GetAllOrdersByStatusRequest;
import ru.candle.store.orderservice.dto.request.order.GetOrderRequest;
import ru.candle.store.orderservice.dto.response.ProductInfo;
import ru.candle.store.orderservice.dto.response.integration.GetProductsInfoResponse;
import ru.candle.store.orderservice.dto.response.integration.GetUserInfoResponse;
import ru.candle.store.orderservice.dto.response.order.*;
import ru.candle.store.orderservice.entity.OrderEntity;
import ru.candle.store.orderservice.entity.ProductEntity;
import ru.candle.store.orderservice.entity.PromocodeEntity;
import ru.candle.store.orderservice.repository.BasketRepository;
import ru.candle.store.orderservice.repository.OrderRepository;
import ru.candle.store.orderservice.repository.PromocodeRepository;
import ru.candle.store.orderservice.service.impl.IntegrationServiceImpl;
import ru.candle.store.orderservice.service.impl.OrderServiceImpl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PromocodeRepository promocodeRepository;

    @Mock
    private BasketRepository basketRepository;

    @Mock
    private IntegrationServiceImpl integrationService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void whenAddOrderWithPromocodeSuccess() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        AddOrderRequest rq = new AddOrderRequest("Address", "promo", List.of(
                new ProductAndCount(1L, 1L),
                new ProductAndCount(2L, 2L)
        ));
        GetProductsInfoResponse productsInfo = new GetProductsInfoResponse(List.of(
                new ProductInfo(1L, "image", "title", "description", "subtitle", 10L, "type", "measure", "unit", true),
                new ProductInfo(2L, "image2", "title2", "description2", "subtitle2", 20L, "type2", "measure2", "unit2", true)
        ));
        List<ProductEntity> productEntities = List.of(
                new ProductEntity(1L, "image", "title", 10L, 9L, 1L),
                new ProductEntity(2L, "image2", "title2", 20L, 18L, 2L)
        );
        OrderEntity orderEntity = new OrderEntity(
                null,
                1L,
                LocalDateTime.now(ZoneId.of("UTC+5")).format(dateTimeFormatter),
                rq.getAddress(),
                rq.getPromocode(),
                50L,
                45L,
                productEntities,
                Status.NEW
        );
        Mockito.when(promocodeRepository.findByPromocode("promo")).thenReturn(new PromocodeEntity("promo", 10L, true));
        Mockito.when(integrationService.getProductInfoByIds(List.of(1L, 2L), "USER")).thenReturn(productsInfo);
        Mockito.when(orderRepository.save(orderEntity)).thenReturn(orderEntity);

        AddOrderResponse rs = orderService.addOrder(rq, 1L, "USER");
        Assertions.assertEquals(new AddOrderResponse(true), rs);
    }

    @Test
    void whenAddOrderWithoutPromocodeSuccess() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        AddOrderRequest rq = new AddOrderRequest("Address", null, List.of(
                new ProductAndCount(1L, 1L),
                new ProductAndCount(2L, 2L)
        ));
        GetProductsInfoResponse productsInfo = new GetProductsInfoResponse(List.of(
                new ProductInfo(1L, "image", "title", "description", "subtitle", 10L, "type", "measure", "unit", true),
                new ProductInfo(2L, "image2", "title2", "description2", "subtitle2", 20L, "type2", "measure2", "unit2", true)
        ));
        List<ProductEntity> productEntities = List.of(
                new ProductEntity(1L, "image", "title", 10L, null, 1L),
                new ProductEntity(2L, "image2", "title2", 20L, null, 2L)
        );
        OrderEntity orderEntity = new OrderEntity(
                null,
                1L,
                LocalDateTime.now(ZoneId.of("UTC+5")).format(dateTimeFormatter),
                rq.getAddress(),
                rq.getPromocode(),
                50L,
                null,
                productEntities,
                Status.NEW
        );
        Mockito.when(integrationService.getProductInfoByIds(List.of(1L, 2L), "USER")).thenReturn(productsInfo);
        Mockito.when(orderRepository.save(orderEntity)).thenReturn(orderEntity);

        AddOrderResponse rs = orderService.addOrder(rq, 1L, "USER");
        Assertions.assertEquals(new AddOrderResponse(true), rs);
    }

    @Test
    void whenAddOrderPromocodeNotFoundFail() {
        AddOrderRequest rq = new AddOrderRequest("Address", "promo", List.of(
                new ProductAndCount(1L, 1L),
                new ProductAndCount(2L, 2L)
        ));
        Mockito.when(promocodeRepository.findByPromocode("promo")).thenReturn(null);

        Assertions.assertThrows(RuntimeException.class, () -> orderService.addOrder(rq, 1L, "USER"));
    }

    @Test
    void whenAddOrderPromocodeNotActualFail() {
        AddOrderRequest rq = new AddOrderRequest("Address", "promo", List.of(
                new ProductAndCount(1L, 1L),
                new ProductAndCount(2L, 2L)
        ));
        Mockito.when(promocodeRepository.findByPromocode("promo")).thenReturn(new PromocodeEntity("promo", 10L, false));

        Assertions.assertThrows(RuntimeException.class, () -> orderService.addOrder(rq, 1L, "USER"));
    }

    @Test
    void whenAddOrderProductsInfoNotCompleteFail() {
        AddOrderRequest rq = new AddOrderRequest("Address", "promo", List.of(
                new ProductAndCount(1L, 1L),
                new ProductAndCount(2L, 2L)
        ));
        GetProductsInfoResponse productsInfo = new GetProductsInfoResponse(List.of(
                new ProductInfo(1L, "image", "title", "description", "subtitle", 10L, "type", "measure", "unit", true)
        ));

        Mockito.when(promocodeRepository.findByPromocode("promo")).thenReturn(new PromocodeEntity("promo", 10L, true));
        Mockito.when(integrationService.getProductInfoByIds(List.of(1L, 2L), "USER")).thenReturn(productsInfo);

        Assertions.assertThrows(RuntimeException.class, () -> orderService.addOrder(rq, 1L, "USER"));
    }

    @Test
    void whenAddOrderProductsInfoHasNotActualFail() {
        AddOrderRequest rq = new AddOrderRequest("Address", "promo", List.of(
                new ProductAndCount(1L, 1L),
                new ProductAndCount(2L, 2L)
        ));
        GetProductsInfoResponse productsInfo = new GetProductsInfoResponse(List.of(
                new ProductInfo(1L, "image", "title", "description", "subtitle", 10L, "type", "measure", "unit", true),
                new ProductInfo(2L, "image", "title", "description", "subtitle", 10L, "type", "measure", "unit", false)
        ));

        Mockito.when(promocodeRepository.findByPromocode("promo")).thenReturn(new PromocodeEntity("promo", 10L, true));
        Mockito.when(integrationService.getProductInfoByIds(List.of(1L, 2L), "USER")).thenReturn(productsInfo);

        Assertions.assertThrows(RuntimeException.class, () -> orderService.addOrder(rq, 1L, "USER"));
    }

    @Test
    void whenGetOrderSuccess() {
        GetOrderRequest rq = new GetOrderRequest(1L);
        GetUserInfoResponse userInfoResponse = new GetUserInfoResponse("First", "Last", "Middle", "City", "1990-01-01", "Address");
        Mockito.when(integrationService.getUserInfo(1L, "USER")).thenReturn(userInfoResponse);
        List<ProductEntity> products = List.of(
                new ProductEntity(1L, "image", "title", 10L, null, 1L),
                new ProductEntity(2L, "image2", "title2", 20L, null, 2L)
        );
        OrderEntity orderEntity = new OrderEntity(1L, 1L, "2024-02-05 23:00:00", "Address", "promo", 20L, 10L, products, Status.NEW);
        Mockito.when(orderRepository.findByIdAndUserId(1L, 1L)).thenReturn(orderEntity);
        GetOrderResponse expRs = new GetOrderResponse("First", "Last", "Address", "2024-02-05 23:00:00", "promo", 20L, 10L, products, Status.NEW);

        GetOrderResponse actRs = orderService.getOrder(new GetOrderRequest(1L), 1L, "USER");
        Assertions.assertEquals(expRs, actRs);

    }

    @Test
    void whenGetOrderWhenNotAllUserInfoCompleteSuccess() {
        GetOrderRequest rq = new GetOrderRequest(1L);
        GetUserInfoResponse userInfoResponse = new GetUserInfoResponse("First", "Last", null, null, "1990-01-01", "Address");
        Mockito.when(integrationService.getUserInfo(1L, "USER")).thenReturn(userInfoResponse);
        List<ProductEntity> products = List.of(
                new ProductEntity(1L, "image", "title", 10L, null, 1L),
                new ProductEntity(2L, "image2", "title2", 20L, null, 2L)
        );
        OrderEntity orderEntity = new OrderEntity(null, 1L, "2024-02-05 23:00:00", "Address", null, 20L, null, products, Status.NEW);
        Mockito.when(orderRepository.findByIdAndUserId(1L, 1L)).thenReturn(orderEntity);
        GetOrderResponse expRs = new GetOrderResponse("First", "Last", "Address", "2024-02-05 23:00:00", null, 20L, null, products, Status.NEW);

        GetOrderResponse actRs = orderService.getOrder(new GetOrderRequest(1L), 1L, "USER");
        Assertions.assertEquals(expRs, actRs);

    }

    @Test
    void whenGetOrderNotFoundFail() {
        GetOrderRequest rq = new GetOrderRequest(1L);
        GetUserInfoResponse userInfoResponse = new GetUserInfoResponse("First", "Last", null, null, "1990-01-01", "Address");
        Mockito.when(integrationService.getUserInfo(1L, "USER")).thenReturn(userInfoResponse);
        List<ProductEntity> products = List.of(
                new ProductEntity(1L, "image", "title", 10L, null, 1L),
                new ProductEntity(2L, "image2", "title2", 20L, null, 2L)
        );
        Mockito.when(orderRepository.findByIdAndUserId(1L, 1L)).thenReturn(null);
        GetOrderResponse expRs = new GetOrderResponse("First", "Last", "Address", "2024-02-05 23:00:00", null, 20L, null, products, Status.NEW);

        Assertions.assertThrows(RuntimeException.class, () -> orderService.getOrder(new GetOrderRequest(1L), 1L, "USER"));
    }

    @Test
    void whenChangeOrderStatusSuccess() {
        List<ProductEntity> products = List.of(
                new ProductEntity(1L, "image", "title", 10L, null, 1L),
                new ProductEntity(2L, "image2", "title2", 20L, null, 2L)
        );
        OrderEntity orderEntity = new OrderEntity(1L, 1L, "2024-02-05 23:00:00", "Address", "promo", 20L, 10L, products, Status.NEW);
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(orderEntity));
        orderEntity.setStatus(Status.IN_PROGRESS);
        Mockito.when(orderRepository.save(orderEntity)).thenReturn(orderEntity);

        ChangeOrderStatusResponse rs = orderService.changeOrderStatus(new ChangeOrderStatusRequest(1L, Status.IN_PROGRESS));
        Assertions.assertEquals(new ChangeOrderStatusResponse(true), rs);
    }

    @Test
    void whenChangeOrderStatusNotFoundFail() {
        Mockito.when(orderRepository.findById(1L)).thenReturn(null);

        Assertions.assertThrows(RuntimeException.class, () -> orderService.changeOrderStatus(new ChangeOrderStatusRequest(1L, Status.IN_PROGRESS)));
    }

    @Test
    void whenGetAllOrdersByStatusSuccess() {
        List<ProductEntity> products = List.of(
                new ProductEntity(1L, "image", "title", 10L, null, 1L),
                new ProductEntity(2L, "image2", "title2", 20L, null, 2L)
        );
        List<OrderEntity> orderEntities = List.of(
                new OrderEntity(1L, 1L, "2024-02-05 23:00:00", "Address", "promo", 20L, 10L, products, Status.NEW),
                new OrderEntity(2L, 1L, "2024-02-05 23:00:00", "Address", "promo", 20L, 10L, products, Status.NEW)
        );
        Mockito.when(orderRepository.findAllByStatus(Status.NEW)).thenReturn(orderEntities);
        List<Order> orders = List.of(
                new Order(1L, "2024-02-05 23:00:00",20L, 10L, Status.NEW ),
                new Order(2L, "2024-02-05 23:00:00",20L, 10L, Status.NEW )
        );
        GetAllOrdersByStatusResponse expRs = new GetAllOrdersByStatusResponse(orders);

        GetAllOrdersByStatusResponse actRs = orderService.getAllOrdersByStatus(new GetAllOrdersByStatusRequest(Status.NEW));
        Assertions.assertEquals(expRs, actRs);
    }

    @Test
    void whenGetAllOrdersByStatusNotFoundFail() {
        Mockito.when(orderRepository.findAllByStatus(Status.NEW)).thenReturn(null);

        Assertions.assertThrows(RuntimeException.class, () -> orderService.getAllOrdersByStatus(new GetAllOrdersByStatusRequest(Status.NEW)));
    }

    @Test
    void whenGetOrderListSuccess() {
        List<ProductEntity> products = List.of(
                new ProductEntity(1L, "image", "title", 10L, null, 1L),
                new ProductEntity(2L, "image2", "title2", 20L, null, 2L)
        );
        List<OrderEntity> orderEntities = List.of(
                new OrderEntity(1L, 1L, "2024-02-05 23:00:00", "Address", "promo", 20L, 10L, products, Status.NEW),
                new OrderEntity(2L, 1L, "2024-02-05 23:00:00", "Address", "promo", 20L, 10L, products, Status.NEW)
        );
        Mockito.when(orderRepository.findAllByUserId(1L)).thenReturn(orderEntities);
        List<Order> orders = List.of(
                new Order(1L, "2024-02-05 23:00:00",20L, 10L, Status.NEW ),
                new Order(2L, "2024-02-05 23:00:00",20L, 10L, Status.NEW )
        );
        GetOrderListResponse expRs = new GetOrderListResponse(orders);

        GetOrderListResponse actRs = orderService.getOrderList(1L);
        Assertions.assertEquals(expRs, actRs);
    }

    @Test
    void whenGetOrderListEmptyFail() {
        Mockito.when(orderRepository.findAllByUserId(1L)).thenReturn(null);

        Assertions.assertThrows(RuntimeException.class, () -> orderService.getOrderList(1L));
    }

}
