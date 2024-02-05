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
        Mockito.when(service.addProduct(rq, 1L, "USER")).thenReturn(new AddProductResponse(true));
        AddProductResponse rs = new AddProductResponse(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/basket/add").header("role", "USER").header("userId", 1L).content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenAddProductFail() throws Exception {
        AddProductRequest rq = new AddProductRequest(1L, 2L);
        Mockito.when(service.addProduct(rq, 1L, "USER")).thenReturn(new AddProductResponse(true));

        mockMvc.perform(MockMvcRequestBuilders.post("/basket/add").header("role", "USER").content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenDeleteProductSuccess() throws Exception {
        DeleteProductRequest rq = new DeleteProductRequest(1L);
        Mockito.when(service.deleteProduct(rq, 1L)).thenReturn(new DeleteProductResponse(true));
        DeleteProductResponse rs = new DeleteProductResponse(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/basket/delete").header("role", "USER").header("userId", 1L).content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenDeleteProductFail() throws Exception {
        DeleteProductRequest rq = new DeleteProductRequest(1L);
        Mockito.when(service.deleteProduct(rq, 1L)).thenReturn(new DeleteProductResponse(true));

        mockMvc.perform(MockMvcRequestBuilders.post("/basket/delete").header("role", "USER").content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenProductCountChangeSuccess() throws Exception {
        ChangeCountProductRequest rq = new ChangeCountProductRequest(1L, 1L);
        Mockito.when(service.changeCountProduct(rq, 1L)).thenReturn(new ChangeCountProductResponse(true));
        ChangeCountProductResponse rs = new ChangeCountProductResponse(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/basket/product/count/change").header("role", "USER").header("userId", 1L).content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenProductCountChangeFail() throws Exception {
        ChangeCountProductRequest rq = new ChangeCountProductRequest(1L, 1L);
        Mockito.when(service.changeCountProduct(rq, 1L)).thenReturn(new ChangeCountProductResponse(true));

        mockMvc.perform(MockMvcRequestBuilders.post("/basket/product/count/change").header("role", "USER").content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenDeleteAllProductsSuccess() throws Exception {
        Mockito.when(service.deleteAllProduct(1L)).thenReturn(new DeleteAllProductResponse(true));
        DeleteAllProductResponse rs = new DeleteAllProductResponse(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/basket/clear").header("role", "USER").header("userId", 1L).content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenDeleteAllProductsFail() throws Exception {
        Mockito.when(service.deleteAllProduct(1L)).thenReturn(new DeleteAllProductResponse(true));

        mockMvc.perform(MockMvcRequestBuilders.post("/basket/product/count/change").header("role", "USER").content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenGetBasketSuccess() throws Exception {
        List<Product> products = List.of(
                new Product(1L, "image", "title", 1L, 2L, 3L),
                new Product(2L, "image1", "title1", 2L, 3L, 4L)
        );
        Mockito.when(service.getBasket(1L, "USER")).thenReturn(new GetBasketResponse(products, 20L));
        GetBasketResponse rs = new GetBasketResponse(products, 20L);

        mockMvc.perform(MockMvcRequestBuilders.get("/basket/get").header("role", "USER").header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenGetBasketFail() throws Exception {
        List<Product> products = List.of(
                new Product(1L, "image", "title", 1L, 2L, 3L),
                new Product(2L, "image1", "title1", 2L, 3L, 4L)
        );
        Mockito.when(service.getBasket(1L, "USER")).thenReturn(new GetBasketResponse(products, 20L));

        mockMvc.perform(MockMvcRequestBuilders.get("/basket/get").header("role", "USER"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenPromocodeApplySuccess() throws Exception {
        ApplyPromocodeRequest rq = new ApplyPromocodeRequest("promo");
        List<Product> products = List.of(
                new Product(1L, "image", "title", 1L, 2L, 3L),
                new Product(2L, "image1", "title1", 2L, 3L, 4L)
        );
        Mockito.when(service.applyPromocode(rq, 1L, "USER")).thenReturn(new ApplyPromocodeResponse(products, 20L, 10L));
        ApplyPromocodeResponse rs = new ApplyPromocodeResponse(products, 20L, 10L);

        mockMvc.perform(MockMvcRequestBuilders.post("/basket/promocode/apply").header("role", "USER").header("userId", 1L).content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rs)));
    }

    @Test
    void whenPromocodeApplyFail() throws Exception {
        ApplyPromocodeRequest rq = new ApplyPromocodeRequest("promo");
        List<Product> products = List.of(
                new Product(1L, "image", "title", 1L, 2L, 3L),
                new Product(2L, "image1", "title1", 2L, 3L, 4L)
        );
        Mockito.when(service.applyPromocode(rq, 1L, "USER")).thenReturn(new ApplyPromocodeResponse(products, 20L, 10L));

        mockMvc.perform(MockMvcRequestBuilders.post("/basket/promocode/apply").header("role", "USER").content(objectMapper.writeValueAsString(rq)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}