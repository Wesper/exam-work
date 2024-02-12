package ru.candle.store.orderservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.candle.store.orderservice.config.OrderConfig;
import ru.candle.store.orderservice.controller.OrderController;
import ru.candle.store.orderservice.dictionary.ExceptionCode;
import ru.candle.store.orderservice.dictionary.Status;
import ru.candle.store.orderservice.dto.Order;
import ru.candle.store.orderservice.dto.request.ProductAndCount;
import ru.candle.store.orderservice.dto.request.order.AddOrderRequest;
import ru.candle.store.orderservice.dto.request.order.ChangeOrderStatusRequest;
import ru.candle.store.orderservice.dto.request.order.GetAllOrdersByStatusRequest;
import ru.candle.store.orderservice.dto.request.order.GetOrderRequest;
import ru.candle.store.orderservice.dto.response.order.*;
import ru.candle.store.orderservice.entity.ProductEntity;
import ru.candle.store.orderservice.service.impl.OrderServiceImpl;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
@AutoConfigureMockMvc()
@Import(OrderConfig.class)
public class
OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderServiceImpl service;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void whenAddOrderSuccess() throws Exception {
        List<ProductAndCount> productsAndCounts = List.of(
                new ProductAndCount(1L, 1L),
                new ProductAndCount(2L, 2L)
        );
        AddOrderRequest rq = new AddOrderRequest("address", "promo", productsAndCounts);
        Mockito.when(service.addOrder(rq, 1L, "USER")).thenReturn(AddOrderResponse.builder().success(true).build());
        AddOrderResponse rs = AddOrderResponse.builder().success(true).build();

        mockMvc.perform(MockMvcRequestBuilders.post("/order/add").header("role", "USER")
                        .header("userId", 1L).content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenAddOrderFail() throws Exception {
        List<ProductAndCount> productsAndCounts = List.of(
                new ProductAndCount(1L, 1L),
                new ProductAndCount(2L, 2L)
        );
        AddOrderRequest rq = new AddOrderRequest("address", "promo", productsAndCounts);
        AddOrderResponse rs = AddOrderResponse.builder()
                .success(false)
                .errorCode(ExceptionCode.ORDER_NOT_FOUND.getErrorCode())
                .errorText(ExceptionCode.ORDER_NOT_FOUND.getErrorText())
                .build();
        Mockito.when(service.addOrder(rq, 1L, "USER")).thenReturn(rs);

        mockMvc.perform(MockMvcRequestBuilders.post("/order/add").header("role", "USER")
                        .header("userId", 1L).content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenGetOrderSuccess() throws Exception {
        List<ProductEntity> products = List.of(
                new ProductEntity(1L, "image", "title", 1L, 2L, 3L),
                new ProductEntity(2L, "image1", "title1", 2L, 3L, 4L)
        );
        GetOrderRequest rq = new GetOrderRequest(1L);
        GetOrderResponse rs = GetOrderResponse.builder()
                .success(true)
                .firstName("First")
                .lastName("Last")
                .address("Address")
                .date("2024-02-05 23:00:00")
                .promocode("promo")
                .totalPrice(10L)
                .totalPromoPrice(0L)
                .products(products)
                .status(Status.NEW)
                .build();
        Mockito.when(service.getOrder(rq, 1L, "USER", true)).thenReturn(rs);

        mockMvc.perform(MockMvcRequestBuilders.post("/order/get").header("role", "USER")
                        .header("userId", 1L).content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenGetOrderFail() throws Exception {
        List<ProductEntity> products = List.of(
                new ProductEntity(1L, "image", "title", 1L, 2L, 3L),
                new ProductEntity(2L, "image1", "title1", 2L, 3L, 4L)
        );
        GetOrderRequest rq = new GetOrderRequest(1L);
        GetOrderResponse rs = GetOrderResponse.builder()
                .success(false)
                .errorCode(ExceptionCode.PRODUCT_NOT_FOUND.getErrorCode())
                .errorText(ExceptionCode.PRODUCT_NOT_FOUND.getErrorText())
                .build();
        Mockito.when(service.getOrder(rq, 1L, "USER", true)).thenReturn(rs);


        mockMvc.perform(MockMvcRequestBuilders.post("/order/get").header("role", "USER")
                        .header("userId", 1L).content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenGetOrderListSuccess() throws Exception {
        List<Order> orders = List.of(
                new Order(1L, "2024-02-05 23:00:00", 1L, 1L, Status.NEW),
                new Order(2L, "2024-02-05 23:00:00", 1L, null, Status.NEW)
        );
        GetOrderListResponse rs = GetOrderListResponse.builder().success(true).orders(orders).build();
        Mockito.when(service.getOrderList(1L)).thenReturn(rs);

        mockMvc.perform(MockMvcRequestBuilders.get("/order/get").header("role", "USER").header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenGetOrderListFail() throws Exception {
        GetOrderListResponse orders = GetOrderListResponse.builder()
                .success(false)
                .errorCode(ExceptionCode.ORDER_NOT_FOUND.getErrorCode())
                .errorText(ExceptionCode.ORDER_NOT_FOUND.getErrorText())
                .build();
        Mockito.when(service.getOrderList(1L)).thenReturn(orders);

        mockMvc.perform(MockMvcRequestBuilders.get("/order/get").header("role", "USER").header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(orders)));
    }

    @Test
    void whenChangeOrderStatusSuccess() throws Exception {
        ChangeOrderStatusRequest rq = new ChangeOrderStatusRequest(1L, Status.IN_PROGRESS);
        ChangeOrderStatusResponse rs = ChangeOrderStatusResponse.builder().success(true).build();
        Mockito.when(service.changeOrderStatus(rq)).thenReturn(rs);

        mockMvc.perform(MockMvcRequestBuilders.post("/order/status/change").header("role", "MANAGER")
                        .content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenChangeOrderStatusFail() throws Exception {
        ChangeOrderStatusRequest rq = new ChangeOrderStatusRequest(1L, Status.IN_PROGRESS);
        ChangeOrderStatusResponse rs = ChangeOrderStatusResponse.builder()
                .success(false)
                .errorCode(ExceptionCode.ORDER_NOT_FOUND.getErrorCode())
                .errorText(ExceptionCode.ORDER_NOT_FOUND.getErrorText())
                .build();
        Mockito.when(service.changeOrderStatus(rq)).thenReturn(rs);

        mockMvc.perform(MockMvcRequestBuilders.post("/order/status/change").header("role", "MANAGER")
                        .content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenGetAllOrdersByStatusSuccess() throws Exception {
        GetAllOrdersByStatusRequest rq = new GetAllOrdersByStatusRequest(Status.IN_PROGRESS);
        List<Order> orders = List.of(
                new Order(1L, "2024-02-05 23:00:00", 1L, 1L, Status.NEW),
                new Order(2L, "2024-02-05 23:00:00", 1L, null, Status.NEW)
        );
        GetAllOrdersByStatusResponse rs = GetAllOrdersByStatusResponse.builder().success(true).orders(orders).build();
        Mockito.when(service.getAllOrdersByStatus(rq)).thenReturn(rs);

        mockMvc.perform(MockMvcRequestBuilders.post("/order/all/get").header("role", "MANAGER")
                        .content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenGetAllOrdersByStatusFail() throws Exception {
        GetAllOrdersByStatusRequest rq = new GetAllOrdersByStatusRequest(Status.IN_PROGRESS);
        GetAllOrdersByStatusResponse rs = GetAllOrdersByStatusResponse.builder()
                .success(false)
                .errorCode(ExceptionCode.ORDER_NOT_FOUND.getErrorCode())
                .errorText(ExceptionCode.ORDER_NOT_FOUND.getErrorText())
                .build();

        Mockito.when(service.getAllOrdersByStatus(rq)).thenReturn(rs);

        mockMvc.perform(MockMvcRequestBuilders.post("/order/all/get").header("role", "MANAGER")
                        .content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }
}
