package ru.candle.store.orderservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.candle.store.orderservice.dictionary.ExceptionCode;
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
import ru.candle.store.orderservice.exception.OrderException;
import ru.candle.store.orderservice.repository.OrderRepository;
import ru.candle.store.orderservice.repository.PromocodeRepository;
import ru.candle.store.orderservice.service.impl.IntegrationServiceImpl;
import ru.candle.store.orderservice.service.impl.OrderServiceImpl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PromocodeRepository promocodeRepository;

    @Mock
    private IntegrationServiceImpl integrationService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void whenAddOrderWithPromocodeSuccess() throws OrderException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        AddOrderRequest rq = new AddOrderRequest("Address", "promo", List.of(
                new ProductAndCount(1L, 1L),
                new ProductAndCount(2L, 2L)
        ));
        List<ProductInfo> productsInfo = List.of(
                new ProductInfo(1L, "image", "title", "description", "subtitle",
                        10L, "type", "measure", "unit", true),
                new ProductInfo(2L, "image2", "title2", "description2", "subtitle2",
                        20L, "type2", "measure2", "unit2", true)
        );
        GetProductsInfoResponse productsInfoResponse = GetProductsInfoResponse.builder().success(true).productsInfo(productsInfo).build();
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
        Mockito.when(integrationService.getProductInfoByIds(List.of(1L, 2L), "USER")).thenReturn(productsInfoResponse);
        Mockito.when(orderRepository.save(orderEntity)).thenReturn(orderEntity);

        AddOrderResponse rs = orderService.addOrder(rq, 1L, "USER");
        Assertions.assertEquals(AddOrderResponse.builder().success(true).build(), rs);
    }

    @Test
    void whenAddOrderWithoutPromocodeSuccess() throws OrderException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        AddOrderRequest rq = new AddOrderRequest("Address", null, List.of(
                new ProductAndCount(1L, 1L),
                new ProductAndCount(2L, 2L)
        ));
        List<ProductInfo> productsInfo = List.of(
                new ProductInfo(1L, "image", "title", "description", "subtitle", 10L, "type", "measure", "unit", true),
                new ProductInfo(2L, "image2", "title2", "description2", "subtitle2", 20L, "type2", "measure2", "unit2", true)
        );
        GetProductsInfoResponse productsInfoResponse = GetProductsInfoResponse.builder().success(true).productsInfo(productsInfo).build();
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
        Mockito.when(integrationService.getProductInfoByIds(List.of(1L, 2L), "USER")).thenReturn(productsInfoResponse);
        Mockito.when(orderRepository.save(orderEntity)).thenReturn(orderEntity);

        AddOrderResponse rs = orderService.addOrder(rq, 1L, "USER");
        Assertions.assertEquals(AddOrderResponse.builder().success(true).build(), rs);
    }

    @Test
    void whenAddOrderPromocodeNotFoundFail() {
        AddOrderRequest rq = new AddOrderRequest("Address", "promo", List.of(
                new ProductAndCount(1L, 1L),
                new ProductAndCount(2L, 2L)
        ));
        Mockito.when(promocodeRepository.findByPromocode("promo")).thenReturn(null);

        AddOrderResponse rs = orderService.addOrder(rq, 1L, "USER");
        Assertions.assertAll(
                () -> Assertions.assertFalse(rs.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.PROMOCODE_NOT_FOUND.getErrorCode(), rs.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.PROMOCODE_NOT_FOUND.getErrorText(), rs.getErrorText())
        );
    }

    @Test
    void whenAddOrderPromocodeNotActualFail() {
        AddOrderRequest rq = new AddOrderRequest("Address", "promo", List.of(
                new ProductAndCount(1L, 1L),
                new ProductAndCount(2L, 2L)
        ));
        Mockito.when(promocodeRepository.findByPromocode("promo")).thenReturn(new PromocodeEntity("promo", 10L, false));

        AddOrderResponse rs = orderService.addOrder(rq, 1L, "USER");
        Assertions.assertAll(
                () -> Assertions.assertFalse(rs.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.PROMOCODE_NOT_ACTUAL.getErrorCode(), rs.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.PROMOCODE_NOT_ACTUAL.getErrorText(), rs.getErrorText())
        );
    }

    @Test
    void whenAddOrderProductsInfoNotCompleteFail() throws OrderException {
        AddOrderRequest rq = new AddOrderRequest("Address", "promo", List.of(
                new ProductAndCount(1L, 1L),
                new ProductAndCount(2L, 2L)
        ));
        List<ProductInfo> productsInfo = List.of(
                new ProductInfo(1L, "image", "title", "description", "subtitle",
                        10L, "type", "measure", "unit", true)
        );
        GetProductsInfoResponse productsInfoResponse = GetProductsInfoResponse.builder().success(true).productsInfo(productsInfo).build();

        Mockito.when(promocodeRepository.findByPromocode("promo")).thenReturn(new PromocodeEntity("promo", 10L, true));
        Mockito.when(integrationService.getProductInfoByIds(List.of(1L, 2L), "USER")).thenReturn(productsInfoResponse);

        AddOrderResponse rs = orderService.addOrder(rq, 1L, "USER");
        Assertions.assertAll(
                () -> Assertions.assertFalse(rs.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.GET_PRODUCTS_INFO_NOT_COMPLETE.getErrorCode(), rs.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.GET_PRODUCTS_INFO_NOT_COMPLETE.getErrorText(), rs.getErrorText())
        );
    }

    @Test
    void whenAddOrderProductsInfoHasNotActualFail() throws OrderException {
        AddOrderRequest rq = new AddOrderRequest("Address", "promo", List.of(
                new ProductAndCount(1L, 1L),
                new ProductAndCount(2L, 2L)
        ));
        List<ProductInfo> productsInfo = List.of(
                new ProductInfo(1L, "image", "title", "description", "subtitle", 10L, "type", "measure", "unit", true),
                new ProductInfo(2L, "image", "title", "description", "subtitle", 10L, "type", "measure", "unit", false)
        );
        GetProductsInfoResponse productsInfoResponse = GetProductsInfoResponse.builder().success(true).productsInfo(productsInfo).build();

        Mockito.when(promocodeRepository.findByPromocode("promo")).thenReturn(new PromocodeEntity("promo", 10L, true));
        Mockito.when(integrationService.getProductInfoByIds(List.of(1L, 2L), "USER")).thenReturn(productsInfoResponse);

        AddOrderResponse rs = orderService.addOrder(rq, 1L, "USER");
        Assertions.assertAll(
                () -> Assertions.assertFalse(rs.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.PART_OF_PRODUCTS_NOT_ACTUAL.getErrorCode(), rs.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.PART_OF_PRODUCTS_NOT_ACTUAL.getErrorText(), rs.getErrorText())
        );
    }

    @Test
    void whenGetOrderSuccess() throws OrderException {
        GetOrderRequest rq = new GetOrderRequest(1L);
        GetUserInfoResponse userInfoResponse = GetUserInfoResponse.builder()
                .success(true)
                .firstName("First")
                .lastName("Last")
                .middleName("Middle")
                .city("City")
                .birthday("1990-01-01")
                .address("Address")
                .build();
        Mockito.when(integrationService.getUserInfo(1L, "USER")).thenReturn(userInfoResponse);
        List<ProductEntity> products = List.of(
                new ProductEntity(1L, "image", "title", 10L, null, 1L),
                new ProductEntity(2L, "image2", "title2", 20L, null, 2L)
        );
        OrderEntity orderEntity = new OrderEntity(1L, 1L, "2024-02-05 23:00:00", "Address",
                "promo", 20L, 10L, products, Status.NEW);
        Mockito.when(orderRepository.findByIdAndUserId(1L, 1L)).thenReturn(orderEntity);
        GetOrderResponse expRs = GetOrderResponse.builder()
                .success(true)
                .firstName("First")
                .lastName("Last")
                .address("Address")
                .date("2024-02-05 23:00:00")
                .promocode("promo")
                .totalPrice(20L)
                .totalPromoPrice(10L)
                .products(products)
                .status(Status.NEW)
                .build();

        GetOrderResponse actRs = orderService.getOrder(rq, 1L, "USER");
        Assertions.assertEquals(expRs, actRs);

    }

    @Test
    void whenGetOrderWhenNotAllUserInfoCompleteSuccess() throws OrderException {
        GetOrderRequest rq = new GetOrderRequest(1L);
        GetUserInfoResponse userInfoResponse = GetUserInfoResponse.builder()
                .success(true)
                .firstName("First")
                .lastName("Last")
                .birthday("1990-01-01")
                .address("Address")
                .build();
        Mockito.when(integrationService.getUserInfo(1L, "USER")).thenReturn(userInfoResponse);
        List<ProductEntity> products = List.of(
                new ProductEntity(1L, "image", "title", 10L, null, 1L),
                new ProductEntity(2L, "image2", "title2", 20L, null, 2L)
        );
        OrderEntity orderEntity = new OrderEntity(null, 1L, "2024-02-05 23:00:00", "Address",
                null, 20L, null, products, Status.NEW);
        Mockito.when(orderRepository.findByIdAndUserId(1L, 1L)).thenReturn(orderEntity);
        GetOrderResponse expRs = GetOrderResponse.builder()
                .success(true)
                .firstName("First")
                .lastName("Last")
                .address("Address")
                .date("2024-02-05 23:00:00")
                .totalPrice(20L)
                .products(products)
                .status(Status.NEW)
                .build();

        GetOrderResponse actRs = orderService.getOrder(rq, 1L, "USER");
        Assertions.assertEquals(expRs, actRs);

    }

    @Test
    void whenGetOrderNotFoundFail() throws OrderException {
        GetOrderRequest rq = new GetOrderRequest(1L);
        GetUserInfoResponse userInfoResponse = GetUserInfoResponse.builder()
                .success(true)
                .firstName("First")
                .lastName("Last")
                .birthday("1990-01-01")
                .address("Address")
                .build();
        Mockito.when(integrationService.getUserInfo(1L, "USER")).thenReturn(userInfoResponse);
        List<ProductEntity> products = List.of(
                new ProductEntity(1L, "image", "title", 10L, null, 1L),
                new ProductEntity(2L, "image2", "title2", 20L, null, 2L)
        );
        Mockito.when(orderRepository.findByIdAndUserId(1L, 1L)).thenReturn(null);

        GetOrderResponse rs =  orderService.getOrder(new GetOrderRequest(1L), 1L, "USER");
        Assertions.assertAll(
                () -> Assertions.assertFalse(rs.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.ORDER_NOT_FOUND.getErrorCode(), rs.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.ORDER_NOT_FOUND.getErrorText(), rs.getErrorText())
        );
    }

    @Test
    void whenChangeOrderStatusSuccess() {
        List<ProductEntity> products = List.of(
                new ProductEntity(1L, "image", "title", 10L, null, 1L),
                new ProductEntity(2L, "image2", "title2", 20L, null, 2L)
        );
        OrderEntity orderEntity = new OrderEntity(1L, 1L, "2024-02-05 23:00:00", "Address",
                "promo", 20L, 10L, products, Status.NEW);
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(orderEntity));
        orderEntity.setStatus(Status.IN_PROGRESS);
        Mockito.when(orderRepository.save(orderEntity)).thenReturn(orderEntity);

        ChangeOrderStatusResponse rs = orderService.changeOrderStatus(new ChangeOrderStatusRequest(1L, Status.IN_PROGRESS));
        Assertions.assertEquals(ChangeOrderStatusResponse.builder().success(true).build(), rs);
    }

    @Test
    void whenChangeOrderStatusNotFoundFail() {
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.ofNullable(null));

        ChangeOrderStatusResponse rs = orderService.changeOrderStatus(new ChangeOrderStatusRequest(1L, Status.IN_PROGRESS));
        Assertions.assertAll(
                () -> Assertions.assertFalse(rs.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.ORDER_NOT_FOUND.getErrorCode(), rs.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.ORDER_NOT_FOUND.getErrorText(), rs.getErrorText())
        );
    }

    @Test
    void whenGetAllOrdersByStatusSuccess() {
        List<ProductEntity> products = List.of(
                new ProductEntity(1L, "image", "title", 10L, null, 1L),
                new ProductEntity(2L, "image2", "title2", 20L, null, 2L)
        );
        List<OrderEntity> orderEntities = List.of(
                new OrderEntity(1L, 1L, "2024-02-05 23:00:00", "Address", "promo",
                        20L, 10L, products, Status.NEW),
                new OrderEntity(2L, 1L, "2024-02-05 23:00:00", "Address", "promo",
                        20L, 10L, products, Status.NEW)
        );
        Mockito.when(orderRepository.findAllByStatus(Status.NEW)).thenReturn(orderEntities);
        List<Order> orders = List.of(
                new Order(1L, "2024-02-05 23:00:00", 20L, 10L, Status.NEW),
                new Order(2L, "2024-02-05 23:00:00", 20L, 10L, Status.NEW)
        );
        GetAllOrdersByStatusResponse expRs = GetAllOrdersByStatusResponse.builder().success(true).orders(orders).build();

        GetAllOrdersByStatusResponse actRs = orderService.getAllOrdersByStatus(new GetAllOrdersByStatusRequest(Status.NEW));
        Assertions.assertEquals(expRs, actRs);
    }

    @Test
    void whenGetAllOrdersByStatusNotFoundFail() {
        Mockito.when(orderRepository.findAllByStatus(Status.NEW)).thenReturn(new ArrayList<>());

        GetAllOrdersByStatusResponse rs = orderService.getAllOrdersByStatus(new GetAllOrdersByStatusRequest(Status.NEW));
        Assertions.assertAll(
                () -> Assertions.assertFalse(rs.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.ORDER_IN_SEARCH_STATUS_NOT_FOUND.getErrorCode(), rs.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.ORDER_IN_SEARCH_STATUS_NOT_FOUND.getErrorText(), rs.getErrorText())
        );
    }

    @Test
    void whenGetOrderListSuccess() {
        List<ProductEntity> products = List.of(
                new ProductEntity(1L, "image", "title", 10L, null, 1L),
                new ProductEntity(2L, "image2", "title2", 20L, null, 2L)
        );
        List<OrderEntity> orderEntities = List.of(
                new OrderEntity(1L, 1L, "2024-02-05 23:00:00", "Address", "promo",
                        20L, 10L, products, Status.NEW),
                new OrderEntity(2L, 1L, "2024-02-05 23:00:00", "Address", "promo",
                        20L, 10L, products, Status.NEW)
        );
        Mockito.when(orderRepository.findAllByUserId(1L)).thenReturn(orderEntities);
        List<Order> orders = List.of(
                new Order(1L, "2024-02-05 23:00:00", 20L, 10L, Status.NEW),
                new Order(2L, "2024-02-05 23:00:00", 20L, 10L, Status.NEW)
        );
        GetOrderListResponse expRs = GetOrderListResponse.builder().success(true).orders(orders).build();

        GetOrderListResponse actRs = orderService.getOrderList(1L);
        Assertions.assertEquals(expRs, actRs);
    }

    @Test
    void whenGetOrderListEmptyFail() {
        Mockito.when(orderRepository.findAllByUserId(1L)).thenReturn(new ArrayList<>());

        GetOrderListResponse rs = orderService.getOrderList(1L);
        Assertions.assertAll(
                () -> Assertions.assertFalse(rs.getSuccess()),
                () -> Assertions.assertEquals(ExceptionCode.USER_DONT_HAVE_ORDERS.getErrorCode(), rs.getErrorCode()),
                () -> Assertions.assertEquals(ExceptionCode.USER_DONT_HAVE_ORDERS.getErrorText(), rs.getErrorText())
        );
    }

}
