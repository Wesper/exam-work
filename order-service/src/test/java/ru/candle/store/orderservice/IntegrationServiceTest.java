package ru.candle.store.orderservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.candle.store.orderservice.dto.request.integration.GetProductsInfoRequest;
import ru.candle.store.orderservice.dto.response.ProductInfo;
import ru.candle.store.orderservice.dto.response.integration.GetProductsInfoResponse;
import ru.candle.store.orderservice.dto.response.integration.GetUserInfoResponse;
import ru.candle.store.orderservice.service.impl.IntegrationServiceImpl;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class IntegrationServiceTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    IntegrationServiceImpl integrationService;

    @Test
    void whenGetProductInfoByIdsSuccess() {
        GetProductsInfoRequest request = new GetProductsInfoRequest(List.of(1L, 2L));
        HttpHeaders headers = new HttpHeaders();
        headers.set("role", "MANAGER");
        HttpEntity<GetProductsInfoRequest> entity = new HttpEntity<>(request, headers);
        GetProductsInfoResponse expResponse = new GetProductsInfoResponse(new ArrayList<>());
        expResponse.getProductsInfo().add(new ProductInfo(1L, "image", "title", "description", "subtitle", 1L, "type", "measure", "unit", true));
        expResponse.getProductsInfo().add(new ProductInfo(2L, "image2", "title2", "description2", "subtitle2", 2L, "type2", "measure2", "unit2", false));
        Mockito.when(restTemplate.postForObject("http://product-mplace-service/product/info/get", entity, GetProductsInfoResponse.class)).thenReturn(expResponse);

        Assertions.assertEquals(expResponse, integrationService.getProductInfoByIds(List.of(1L, 2L), "MANAGER"));
    }

    @Test
    void whenGetProductInfoByIdsFail() {
        GetProductsInfoRequest request = new GetProductsInfoRequest(List.of(1L, 2L));
        HttpHeaders headers = new HttpHeaders();
        headers.set("role", "MANAGER");
        HttpEntity<GetProductsInfoRequest> entity = new HttpEntity<>(request, headers);
        Mockito.when(restTemplate.postForObject("http://product-mplace-service/product/info/get", entity, GetProductsInfoResponse.class)).thenReturn(null);

        Assertions.assertThrows(RuntimeException.class, () -> integrationService.getProductInfoByIds(List.of(1L, 2L), "MANAGER"));
    }

    @Test
    void whenGetUserInfoSuccess() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("userId", String.valueOf(1L));
        headers.set("role", "MANAGER");
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        GetUserInfoResponse body = new GetUserInfoResponse("First", "Last", "Middle", "City", "1990-01-01", "Address");
        ResponseEntity<GetUserInfoResponse> expResponse = new ResponseEntity<>(body, HttpStatusCode.valueOf(200));
        Mockito.when(restTemplate.exchange("http://profile-service/profile/get", HttpMethod.GET, entity, GetUserInfoResponse.class)).thenReturn(expResponse);

        Assertions.assertEquals(expResponse.getBody(), integrationService.getUserInfo(1L, "MANAGER"));
    }

    @Test
    void whenGetUserInfoFail() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("userId", String.valueOf(1L));
        headers.set("role", "MANAGER");
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        Mockito.when(restTemplate.exchange("http://profile-service/profile/get", HttpMethod.GET, entity, GetUserInfoResponse.class)).thenThrow(RuntimeException.class);

        Assertions.assertThrows(RuntimeException.class, () -> integrationService.getUserInfo(1L, "MANAGER"));
    }

}
