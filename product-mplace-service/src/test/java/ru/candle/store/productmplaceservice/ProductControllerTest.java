package ru.candle.store.productmplaceservice;

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
    void whenRequestGetAllProductsSuccess() throws Exception {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "a.jpeg", "title", "subtitle", 1L, "type", 0.0));
        GetAllProductsResponse response = GetAllProductsResponse.builder()
                .success(true)
                .products(products)
                .build();
        Mockito.when(service.getAllProducts()).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/product/get").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void whenRequestGetAllProductsFail() throws Exception {
        GetAllProductsResponse response = GetAllProductsResponse.builder()
                .success(false)
                .errorCode("code")
                .errorText("text")
                .build();
        Mockito.when(service.getAllProducts()).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/product/get").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void whenRequestGetProductCardSuccess() throws Exception {
        Long userId = 1L;
        GetProductCardRequest request = new GetProductCardRequest(1L);
        GetProductCardResponse response = GetProductCardResponse.builder()
                .success(true)
                .productId(1L)
                .imageId("a.jpeg")
                .title("title")
                .description("description")
                .price(1L)
                .measure("measure")
                .unitMeasure("unit")
                .type("type")
                .rating(0.0)
                .actual(true)
                .appreciated(true)
                .review(null).
                build();
        Mockito.when(service.getProductCard(request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/card").header("userId", 1L).content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void whenRequestGetProductCardFail() throws Exception {
        Long userId = 1L;
        GetProductCardRequest request = new GetProductCardRequest(1L);
        GetProductCardResponse response = GetProductCardResponse.builder()
                .success(false)
                .errorCode("code")
                .errorText("text")
                .build();
        Mockito.when(service.getProductCard(request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/card").header("userId", 1L).content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void whenRequestAddProductSuccess() throws Exception {
        AddProductRequest request = new AddProductRequest("a.jpeg", "title", "description", "subtitle", 1L, "type", "measure", "unit", true);
        AddProductResponse response = AddProductResponse.builder().success(true).build();
        Mockito.when(service.addProduct(request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/add").header("role", "ADMIN").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void whenRequestAddProductWithoutHeaderFail() throws Exception {
        AddProductRequest request = new AddProductRequest("a.jpeg", "title", "description", "subtitle", 1L, "type", "measure", "unit", true);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/add").header("role", "USER").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(MockMvcRequestBuilders.post("/product/add").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenRequestAddProductFail() throws Exception {
        AddProductRequest request = new AddProductRequest("a.jpeg", "title", "description", "subtitle", 1L, "type", "measure", "unit", true);
        AddProductResponse response = AddProductResponse.builder()
                .success(false)
                .errorCode("code")
                .errorText("text")
                .build();
        Mockito.when(service.addProduct(request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/add").header("role", "ADMIN").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void whenRequestUpdateProductSuccess() throws Exception {
        UpdateProductRequest request = new UpdateProductRequest(1L, "a.jpeg", "title", "description", "subtitle", 1L, "type", "measure", "unit", true);
        UpdateProductResponse response = UpdateProductResponse.builder().success(true).build();
        Mockito.when(service.updateProduct(request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/update").header("role", "ADMIN").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void whenRequestUpdateProductWithoutHeaderFail() throws Exception {
        UpdateProductRequest request = new UpdateProductRequest(1L, "a.jpeg", "title", "description", "subtitle", 1L, "type", "measure", "unit", true);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/update").header("role", "USER").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(MockMvcRequestBuilders.post("/product/update").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenRequestUpdateProductFail() throws Exception {
        UpdateProductRequest request = new UpdateProductRequest(1L, "a.jpeg", "title", "description", "subtitle", 1L, "type", "measure", "unit", true);
        UpdateProductResponse response = UpdateProductResponse.builder()
                .success(false)
                .errorCode("code")
                .errorText("text")
                .build();

        Mockito.when(service.updateProduct(request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/update").header("role", "ADMIN").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void whenRequestChangeProductAvailableSuccess() throws Exception {
        ChangeProductAvailableRequest request = new ChangeProductAvailableRequest(1L, true);
        ChangeProductAvailableResponse response = ChangeProductAvailableResponse.builder().success(true).build();
        Mockito.when(service.changeProductAvailable(request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/available/change").header("role", "ADMIN").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void whenRequestChangeProductAvailableWithoutHeaderFail() throws Exception {
        ChangeProductAvailableRequest request = new ChangeProductAvailableRequest(1L, true);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/available/change").header("role", "USER").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(MockMvcRequestBuilders.post("/product/available/change").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenRequestChangeProductAvailableFail() throws Exception {
        ChangeProductAvailableRequest request = new ChangeProductAvailableRequest(1L, true);
        ChangeProductAvailableResponse response = ChangeProductAvailableResponse.builder()
                .success(false)
                .errorCode("code")
                .errorText("text")
                .build();

        Mockito.when(service.changeProductAvailable(request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/available/change").header("role", "ADMIN").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void whenRequestAddReviewSuccess() throws Exception {
        Long userId = 1L;
        String role = "USER";
        AddReviewRequest request = new AddReviewRequest(1L, "review");
        AddReviewResponse response = AddReviewResponse.builder().success(true).build();
        Mockito.when(service.addReview(request, userId, role)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/review/add").header("role", "USER").header("userId", 1L).content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void whenRequestAddReviewWithoutHeaderFail() throws Exception {
        AddReviewRequest request = new AddReviewRequest(1L, "review");

        mockMvc.perform(MockMvcRequestBuilders.post("/product/review/add").header("role", "ADMIN").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(MockMvcRequestBuilders.post("/product/review/add").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenRequestAddReviewFail() throws Exception {
        Long userId = 1L;
        String role = "USER";
        AddReviewRequest request = new AddReviewRequest(1L, "review");
        AddReviewResponse response = AddReviewResponse.builder()
                .success(false)
                .errorCode("code")
                .errorText("text")
                .build();
        Mockito.when(service.addReview(request, userId, role)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/review/add").header("role", "USER").header("userId", 1L).content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void whenRequestAddRatingSuccess() throws Exception {
        Long userId = 1L;
        String role = "USER";
        AddRatingRequest request = new AddRatingRequest(1L, 1L);
        AddRatingResponse response = AddRatingResponse.builder().success(true).build();
        Mockito.when(service.addRating(request, userId, role)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/rating/add").header("role", "USER").header("userId", 1L).content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void whenRequestAddRatingWithoutHeaderFail() throws Exception {
        Long userId = 1L;
        AddRatingRequest request = new AddRatingRequest(1L, 1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/rating/add").header("role", "ADMIN").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(MockMvcRequestBuilders.post("/product/rating/add").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenRequestAddRatingFail() throws Exception {
        Long userId = 1L;
        String role = "USER";
        AddRatingRequest request = new AddRatingRequest(1L, 1L);
        AddRatingResponse response = AddRatingResponse.builder()
                .success(false)
                .errorCode("code")
                .errorText("text")
                .build();
        Mockito.when(service.addRating(request, userId, role)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/rating/add").header("role", "USER").header("userId", 1L).content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));    }

}
