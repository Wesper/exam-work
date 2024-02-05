package ru.candle.store.productmplaceservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import ru.candle.store.productmplaceservice.dto.request.IsUserPurchasedProductRequest;
import ru.candle.store.productmplaceservice.dto.response.IsUserPurchasedProductResponse;
import ru.candle.store.productmplaceservice.service.impl.IntegrationServiceImpl;

@ExtendWith(MockitoExtension.class)
public class IntegrationServiceTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    IntegrationServiceImpl integrationService;

    @Test
    void wherIsUserPurchasedProductReturnTrue() {
        IsUserPurchasedProductRequest request = new IsUserPurchasedProductRequest(1L, 1L);
        Mockito.when(restTemplate.postForObject("http://order-service/order/isPurchase", request, IsUserPurchasedProductResponse.class)).thenReturn(new IsUserPurchasedProductResponse(true));

        Assertions.assertTrue(integrationService.isUserPurchasedProduct(1L, 1L));
        Mockito.verify(restTemplate, Mockito.times(1)).postForObject("http://order-service/order/isPurchase", request, IsUserPurchasedProductResponse.class);
    }

    @Test
    void wherIsUserPurchasedProductReturnFalse() {
        IsUserPurchasedProductRequest request = new IsUserPurchasedProductRequest(1L, 1L);
        Mockito.when(restTemplate.postForObject("http://order-service/order/isPurchase", request, IsUserPurchasedProductResponse.class)).thenReturn(new IsUserPurchasedProductResponse(false));

        Assertions.assertFalse(integrationService.isUserPurchasedProduct(1L, 1L));
        Mockito.verify(restTemplate, Mockito.times(1)).postForObject("http://order-service/order/isPurchase", request, IsUserPurchasedProductResponse.class);
    }

    @Test
    void wherIsUserPurchasedProductReturnNull() {
        IsUserPurchasedProductRequest request = new IsUserPurchasedProductRequest(1L, 1L);
        Mockito.when(restTemplate.postForObject("http://order-service/order/isPurchase", request, IsUserPurchasedProductResponse.class)).thenReturn(null);

        Assertions.assertThrows(RuntimeException.class, () -> integrationService.isUserPurchasedProduct(1L, 1L));
        Mockito.verify(restTemplate, Mockito.times(1)).postForObject("http://order-service/order/isPurchase", request, IsUserPurchasedProductResponse.class);
    }
}
