package ru.candle.store.productmplaceservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import ru.candle.store.productmplaceservice.dto.request.IsUserPurchasedProductRequest;
import ru.candle.store.productmplaceservice.dto.response.IsUserPurchasedProductResponse;
import ru.candle.store.productmplaceservice.exception.ProductMplaceException;
import ru.candle.store.productmplaceservice.service.impl.IntegrationServiceImpl;

@ExtendWith(MockitoExtension.class)
public class IntegrationServiceTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    IntegrationServiceImpl integrationService;

    @Test
    void wherIsUserPurchasedProductReturnTrue() throws ProductMplaceException {
        String role = "USER";
        IsUserPurchasedProductRequest request = new IsUserPurchasedProductRequest(1L, 1L);
        HttpHeaders headers = new HttpHeaders();
        headers.set("role", role);
        headers.set("userId", request.getUserId().toString());
        HttpEntity<IsUserPurchasedProductRequest> entity = new HttpEntity<>(request, headers);
        IsUserPurchasedProductResponse response = IsUserPurchasedProductResponse.builder().success(true).isPurchased(true).build();
        Mockito.when(restTemplate.postForObject("http://order-service/order/purchased", entity, IsUserPurchasedProductResponse.class)).thenReturn(response);

        Assertions.assertTrue(integrationService.isUserPurchasedProduct(1L, 1L, role));
        Mockito.verify(restTemplate, Mockito.times(1)).postForObject("http://order-service/order/purchased", entity, IsUserPurchasedProductResponse.class);
    }

    @Test
    void wherIsUserPurchasedProductReturnFalse() throws ProductMplaceException {
        String role = "USER";
        IsUserPurchasedProductRequest request = new IsUserPurchasedProductRequest(1L, 1L);
        HttpHeaders headers = new HttpHeaders();
        headers.set("role", role);
        headers.set("userId", request.getUserId().toString());
        HttpEntity<IsUserPurchasedProductRequest> entity = new HttpEntity<>(request, headers);
        IsUserPurchasedProductResponse response = IsUserPurchasedProductResponse.builder()
                .success(true)
                .isPurchased(false)
                .build();
        Mockito.when(restTemplate.postForObject("http://order-service/order/purchased", entity, IsUserPurchasedProductResponse.class)).thenReturn(response);

        Assertions.assertFalse(integrationService.isUserPurchasedProduct(1L, 1L, "USER"));
        Mockito.verify(restTemplate, Mockito.times(1)).postForObject("http://order-service/order/purchased", entity, IsUserPurchasedProductResponse.class);
    }

    @Test
    void wherIsUserPurchasedProductReturnError() {
        String role = "USER";
        IsUserPurchasedProductRequest request = new IsUserPurchasedProductRequest(1L, 1L);
        HttpHeaders headers = new HttpHeaders();
        headers.set("role", role);
        headers.set("userId", request.getUserId().toString());
        HttpEntity<IsUserPurchasedProductRequest> entity = new HttpEntity<>(request, headers);
        IsUserPurchasedProductResponse response = IsUserPurchasedProductResponse.builder()
                .success(false)
                .errorCode("code")
                .errorText("text")
                .build();
        Mockito.when(restTemplate.postForObject("http://order-service/order/purchased", entity, IsUserPurchasedProductResponse.class)).thenReturn(response);

        Assertions.assertThrows(ProductMplaceException.class, () -> integrationService.isUserPurchasedProduct(1L, 1L, "USER"));
        Mockito.verify(restTemplate, Mockito.times(1)).postForObject("http://order-service/order/purchased", entity, IsUserPurchasedProductResponse.class);
    }

    @Test
    void wherIsUserPurchasedProductReturnNull() {
        String role = "USER";
        IsUserPurchasedProductRequest request = new IsUserPurchasedProductRequest(1L, 1L);
        HttpHeaders headers = new HttpHeaders();
        headers.set("role", role);
        headers.set("userId", request.getUserId().toString());
        HttpEntity<IsUserPurchasedProductRequest> entity = new HttpEntity<>(request, headers);
        Mockito.when(restTemplate.postForObject("http://order-service/order/purchased", entity, IsUserPurchasedProductResponse.class)).thenReturn(null);

        Assertions.assertThrows(ProductMplaceException.class, () -> integrationService.isUserPurchasedProduct(1L, 1L, "USER"));
        Mockito.verify(restTemplate, Mockito.times(1)).postForObject("http://order-service/order/purchased", entity, IsUserPurchasedProductResponse.class);
    }
}
