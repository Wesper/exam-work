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
import ru.candle.store.orderservice.controller.BasketController;
import ru.candle.store.orderservice.dictionary.ExceptionCode;
import ru.candle.store.orderservice.dto.Product;
import ru.candle.store.orderservice.dto.request.basket.AddProductRequest;
import ru.candle.store.orderservice.dto.request.basket.ApplyPromocodeRequest;
import ru.candle.store.orderservice.dto.request.basket.ChangeCountProductRequest;
import ru.candle.store.orderservice.dto.request.basket.DeleteProductRequest;
import ru.candle.store.orderservice.dto.response.basket.*;
import ru.candle.store.orderservice.service.impl.BasketServiceImpl;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BasketController.class)
@AutoConfigureMockMvc()
@Import(OrderConfig.class)
public class BasketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BasketServiceImpl service;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void whenAddProductSuccess() throws Exception {
        AddProductRequest rq = new AddProductRequest(1L, 2L);
        AddProductResponse rs = AddProductResponse.builder().success(true).build();
        Mockito.when(service.addProduct(rq, 1L, "USER")).thenReturn(rs);

        mockMvc.perform(MockMvcRequestBuilders.post("/basket/add").header("role", "USER").header("userId", 1L).content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenAddProductWithoutHeaderFail() throws Exception {
        AddProductRequest rq = new AddProductRequest(1L, 2L);
        AddProductResponse rs = AddProductResponse.builder().success(true).build();
        Mockito.when(service.addProduct(rq, 1L, "USER")).thenReturn(rs);

        mockMvc.perform(MockMvcRequestBuilders.post("/basket/add").header("role", "USER").content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenAddProductFail() throws Exception {
        AddProductRequest rq = new AddProductRequest(1L, 2L);
        AddProductResponse rs = AddProductResponse.builder()
                .success(false)
                .errorCode(ExceptionCode.PRODUCT_NOT_FOUND.getErrorCode())
                .errorText(ExceptionCode.PRODUCT_NOT_FOUND.getErrorText())
                .build();
        Mockito.when(service.addProduct(rq, 1L, "USER")).thenReturn(rs);

        mockMvc.perform(MockMvcRequestBuilders.post("/basket/add").header("role", "USER").header("userId", 1L).content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenDeleteProductSuccess() throws Exception {
        DeleteProductRequest rq = new DeleteProductRequest(1L);
        DeleteProductResponse rs = DeleteProductResponse.builder().success(true).build();
        Mockito.when(service.deleteProduct(rq, 1L)).thenReturn(rs);

        mockMvc.perform(MockMvcRequestBuilders.post("/basket/delete").header("role", "USER").header("userId", 1L).content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenDeleteProductWithoutHeader() throws Exception {
        DeleteProductRequest rq = new DeleteProductRequest(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/basket/delete").header("role", "USER").content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenDeleteProductFail() throws Exception {
        DeleteProductRequest rq = new DeleteProductRequest(1L);
        DeleteProductResponse rs = DeleteProductResponse.builder()
                .success(false)
                .errorCode(ExceptionCode.DELETE_PRODUCT_ERROR.getErrorCode())
                .errorText(ExceptionCode.DELETE_PRODUCT_ERROR.getErrorText())
                .build();
        Mockito.when(service.deleteProduct(rq, 1L)).thenReturn(rs);

        mockMvc.perform(MockMvcRequestBuilders.post("/basket/delete").header("role", "USER").header("userId", 1L).content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenProductCountChangeSuccess() throws Exception {
        ChangeCountProductRequest rq = new ChangeCountProductRequest(1L, 1L);
        ChangeCountProductResponse rs = ChangeCountProductResponse.builder().success(true).build();
        Mockito.when(service.changeCountProduct(rq, 1L)).thenReturn(rs);

        mockMvc.perform(MockMvcRequestBuilders.post("/basket/product/count/change").header("role", "USER").header("userId", 1L).content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenProductCountChangeWithoutHeader() throws Exception {
        ChangeCountProductRequest rq = new ChangeCountProductRequest(1L, 1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/basket/product/count/change").header("role", "USER").content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenProductCountChangeFail() throws Exception {
        ChangeCountProductRequest rq = new ChangeCountProductRequest(1L, 1L);
        ChangeCountProductResponse rs = ChangeCountProductResponse.builder()
                .success(false)
                .errorCode(ExceptionCode.CHANGE_COUNT_PRODUCT_ERROR.getErrorCode())
                .errorText(ExceptionCode.CHANGE_COUNT_PRODUCT_ERROR.getErrorText())
                .build();
        Mockito.when(service.changeCountProduct(rq, 1L)).thenReturn(rs);

        mockMvc.perform(MockMvcRequestBuilders.post("/basket/product/count/change").header("role", "USER").header("userId", 1L).content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenDeleteAllProductsSuccess() throws Exception {
        DeleteAllProductResponse rs = DeleteAllProductResponse.builder().success(true).build();
        Mockito.when(service.deleteAllProduct(1L)).thenReturn(rs);

        mockMvc.perform(MockMvcRequestBuilders.get("/basket/clear").header("role", "USER").header("userId", 1L).content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenDeleteAllProductsWithoutHeader() throws Exception {
       mockMvc.perform(MockMvcRequestBuilders.post("/basket/product/count/change").header("role", "USER").content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenDeleteAllProductsFail() throws Exception {
        DeleteAllProductResponse rs = DeleteAllProductResponse.builder()
                .success(false)
                .errorCode(ExceptionCode.DELETE_PRODUCT_ERROR.getErrorCode())
                .errorText(ExceptionCode.DELETE_PRODUCT_ERROR.getErrorText())
                .build();
        Mockito.when(service.deleteAllProduct(1L)).thenReturn(rs);

        mockMvc.perform(MockMvcRequestBuilders.get("/basket/clear").header("role", "USER").header("userId", 1L).content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenGetBasketSuccess() throws Exception {
        List<Product> products = List.of(
                new Product(1L, "image", "title", 1L, 2L, 3L),
                new Product(2L, "image1", "title1", 2L, 3L, 4L)
        );
        GetBasketResponse rs = GetBasketResponse.builder()
                .success(true)
                .products(products)
                .totalPrice(20L)
                .build();
        Mockito.when(service.getBasket(1L, "USER")).thenReturn(rs);

        mockMvc.perform(MockMvcRequestBuilders.get("/basket/get").header("role", "USER").header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenGetBasketWithoutHeader() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/basket/get").header("role", "USER"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenGetBasketFail() throws Exception {
        GetBasketResponse rs = GetBasketResponse.builder()
                .success(false)
                .errorCode(ExceptionCode.PRODUCT_NOT_FOUND.getErrorCode())
                .errorText(ExceptionCode.PRODUCT_NOT_FOUND.getErrorText())
                .build();
        Mockito.when(service.getBasket(1L, "USER")).thenReturn(rs);

        mockMvc.perform(MockMvcRequestBuilders.get("/basket/get").header("role", "USER").header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenPromocodeApplySuccess() throws Exception {
        ApplyPromocodeRequest rq = new ApplyPromocodeRequest("promo");
        List<Product> products = List.of(
                new Product(1L, "image", "title", 1L, 2L, 3L),
                new Product(2L, "image1", "title1", 2L, 3L, 4L)
        );
        ApplyPromocodeResponse rs = ApplyPromocodeResponse.builder()
                .success(true)
                .products(products)
                .totalPrice(20L)
                .totalPricePromo(10L)
                .build();
        Mockito.when(service.applyPromocode(rq, 1L, "USER")).thenReturn(rs);

        mockMvc.perform(MockMvcRequestBuilders.post("/basket/promocode/apply").header("role", "USER").header("userId", 1L).content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenPromocodeApplyWithoutHeader() throws Exception {
        ApplyPromocodeRequest rq = new ApplyPromocodeRequest("promo");

        mockMvc.perform(MockMvcRequestBuilders.post("/basket/promocode/apply").header("role", "USER").content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    void wherPromocodeApplyFail() throws Exception {
        ApplyPromocodeRequest rq = new ApplyPromocodeRequest("promo");
        List<Product> products = List.of(
                new Product(1L, "image", "title", 1L, 2L, 3L),
                new Product(2L, "image1", "title1", 2L, 3L, 4L)
        );
        ApplyPromocodeResponse rs = ApplyPromocodeResponse.builder()
                .success(false)
                .errorCode(ExceptionCode.PROMOCODE_NOT_ACTUAL.getErrorCode())
                .errorText(ExceptionCode.PROMOCODE_NOT_ACTUAL.getErrorText())
                .build();
        Mockito.when(service.applyPromocode(rq, 1L, "USER")).thenReturn(rs);

        mockMvc.perform(MockMvcRequestBuilders.post("/basket/promocode/apply").header("role", "USER").header("userId", 1L).content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }
}