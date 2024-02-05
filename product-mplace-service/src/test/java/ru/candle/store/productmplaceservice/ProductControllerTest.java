package ru.candle.store.productmplaceservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Assertions;
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
import ru.candle.store.productmplaceservice.config.ProductConfig;
import ru.candle.store.productmplaceservice.controller.ProductController;
import ru.candle.store.productmplaceservice.dto.Product;
import ru.candle.store.productmplaceservice.dto.request.*;
import ru.candle.store.productmplaceservice.dto.response.*;
import ru.candle.store.productmplaceservice.service.impl.ProductServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc()
@Import(ProductConfig.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductServiceImpl service;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void WhenRequestGetAllProductsSuccess() throws Exception {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "a.jpeg", "title", "subtitle", 1L, "type", 0.0));
        GetAllProductsResponse response = new GetAllProductsResponse(true, products);
        Mockito.when(service.getAllProducts()).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/product/get").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void WhenRequestGetAllProductsFail() throws Exception {
        Mockito.when(service.getAllProducts()).thenThrow(RuntimeException.class);

        Assertions.assertThrows(ServletException.class, () -> mockMvc.perform(MockMvcRequestBuilders.get("/product/get").contentType(MediaType.APPLICATION_JSON)));
    }

    @Test
    void WhenRequestGetProductCardSuccess() throws Exception {
        Long userId = 1L;
        GetProductCardRequest request = new GetProductCardRequest(1L);
        GetProductCardResponse response = new GetProductCardResponse(1L, "a.jpeg", "title", "description", 1L, "measure", "unit", "type", 0.0, true, true, null);
        Mockito.when(service.getProductCard(request, userId)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/card").header("userId", 1L).content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void WhenRequestGetProductCardWithoutHeaderFail() throws Exception {
        Long userId = 1L;
        GetProductCardRequest request = new GetProductCardRequest(1L);
        GetProductCardResponse response = new GetProductCardResponse(1L, "a.jpeg", "title", "description", 1L, "measure", "unit", "type", 0.0, true, true, null);
        Mockito.when(service.getProductCard(request, userId)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/card").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void WhenRequestGetProductCardFail() throws Exception {
        Long userId = 1L;
        GetProductCardRequest request = new GetProductCardRequest(1L);
        Mockito.when(service.getProductCard(request, userId)).thenThrow(RuntimeException.class);

        Assertions.assertThrows(ServletException.class, () -> mockMvc.perform(MockMvcRequestBuilders.post("/product/card").header("userId", 1L).content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON)));
    }

    @Test
    void WhenRequestAddProductSuccess() throws Exception {
        AddProductRequest request = new AddProductRequest("a.jpeg", "title", "description", "subtitle", 1L, "type", "measure", "unit", true);
        AddProductResponse response = new AddProductResponse(true);
        Mockito.when(service.addProduct(request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/add").header("role", "ADMIN").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void WhenRequestAddProductWithoutHeaderFail() throws Exception {
        AddProductRequest request = new AddProductRequest("a.jpeg", "title", "description", "subtitle", 1L, "type", "measure", "unit", true);
        AddProductResponse response = new AddProductResponse(true);
        Mockito.when(service.addProduct(request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/add").header("role", "USER").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(MockMvcRequestBuilders.post("/product/add").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void WhenRequestAddProductFail() throws Exception {
        AddProductRequest request = new AddProductRequest("a.jpeg", "title", "description", "subtitle", 1L, "type", "measure", "unit", true);
        Mockito.when(service.addProduct(request)).thenThrow(RuntimeException.class);

        Assertions.assertThrows(ServletException.class, () -> mockMvc.perform(MockMvcRequestBuilders.post("/product/add").header("role", "ADMIN").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON)));
    }

    @Test
    void WhenRequestUpdateProductSuccess() throws Exception {
        UpdateProductRequest request = new UpdateProductRequest(1L, "a.jpeg", "title", "description", "subtitle", 1L, "type", "measure", "unit", true);
        UpdateProductResponse response = new UpdateProductResponse(true);
        Mockito.when(service.updateProduct(request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/update").header("role", "ADMIN").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void WhenRequestUpdateProductWithoutHeaderFail() throws Exception {
        UpdateProductRequest request = new UpdateProductRequest(1L, "a.jpeg", "title", "description", "subtitle", 1L, "type", "measure", "unit", true);
        UpdateProductResponse response = new UpdateProductResponse(true);
        Mockito.when(service.updateProduct(request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/update").header("role", "USER").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(MockMvcRequestBuilders.post("/product/update").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void WhenRequestUpdateProductFail() throws Exception {
        UpdateProductRequest request = new UpdateProductRequest(1L, "a.jpeg", "title", "description", "subtitle", 1L, "type", "measure", "unit", true);
        Mockito.when(service.updateProduct(request)).thenThrow(RuntimeException.class);

        Assertions.assertThrows(ServletException.class, () -> mockMvc.perform(MockMvcRequestBuilders.post("/product/update").header("role", "ADMIN").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON)));
    }

    @Test
    void WhenRequestChangeProductAvailableSuccess() throws Exception {
        ChangeProductAvailableRequest request = new ChangeProductAvailableRequest(1L, true);
        ChangeProductAvailableResponse response = new ChangeProductAvailableResponse(true);
        Mockito.when(service.changeProductAvailable(request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/available/change").header("role", "ADMIN").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void WhenRequestChangeProductAvailableWithoutHeaderFail() throws Exception {
        ChangeProductAvailableRequest request = new ChangeProductAvailableRequest(1L, true);
        ChangeProductAvailableResponse response = new ChangeProductAvailableResponse(true);
        Mockito.when(service.changeProductAvailable(request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/available/change").header("role", "USER").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(MockMvcRequestBuilders.post("/product/available/change").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void WhenRequestChangeProductAvailableFail() throws Exception {
        ChangeProductAvailableRequest request = new ChangeProductAvailableRequest(1L, true);
        Mockito.when(service.changeProductAvailable(request)).thenThrow(RuntimeException.class);

        Assertions.assertThrows(ServletException.class, () -> mockMvc.perform(MockMvcRequestBuilders.post("/product/available/change").header("role", "ADMIN").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON)));
    }

    @Test
    void WhenRequestAddReviewSuccess() throws Exception {
        Long userId = 1L;
        AddReviewRequest request = new AddReviewRequest(1L, "review");
        AddReviewResponse response = new AddReviewResponse(true);
        Mockito.when(service.addReview(request, userId)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/review/add").header("role", "USER").header("userId", 1L).content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void WhenRequestAddReviewWithoutHeaderFail() throws Exception {
        Long userId = 1L;
        AddReviewRequest request = new AddReviewRequest(1L, "review");
        AddReviewResponse response = new AddReviewResponse(true);
        Mockito.when(service.addReview(request, userId)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/review/add").header("role", "ADMIN").header("userId", 1L).content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(MockMvcRequestBuilders.post("/product/review/add").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void WhenRequestAddReviewFail() throws Exception {
        Long userId = 1L;
        AddReviewRequest request = new AddReviewRequest(1L, "review");
        Mockito.when(service.addReview(request, userId)).thenThrow(RuntimeException.class);

        Assertions.assertThrows(ServletException.class, () -> mockMvc.perform(MockMvcRequestBuilders.post("/product/review/add").header("role", "USER").header("userId", 1L).content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON)));
    }

    @Test
    void WhenRequestAddRatingSuccess() throws Exception {
        Long userId = 1L;
        AddRatingRequest request = new AddRatingRequest(1L, 1L);
        AddRatingResponse response = new AddRatingResponse(true);
        Mockito.when(service.addRating(request, userId)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/rating/add").header("role", "USER").header("userId", 1L).content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void WhenRequestAddRatingWithoutHeaderFail() throws Exception {
        Long userId = 1L;
        AddRatingRequest request = new AddRatingRequest(1L, 1L);
        AddRatingResponse response = new AddRatingResponse(true);
        Mockito.when(service.addRating(request, userId)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/rating/add").header("role", "ADMIN").header("userId", 1L).content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(MockMvcRequestBuilders.post("/product/rating/add").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void WhenRequestAddRatingFail() throws Exception {
        Long userId = 1L;
        AddRatingRequest request = new AddRatingRequest(1L, 1L);
        Mockito.when(service.addRating(request, userId)).thenThrow(RuntimeException.class);

        Assertions.assertThrows(ServletException.class, () -> mockMvc.perform(MockMvcRequestBuilders.post("/product/rating/add").header("role", "USER").header("userId", 1L).content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON)));
    }

}
