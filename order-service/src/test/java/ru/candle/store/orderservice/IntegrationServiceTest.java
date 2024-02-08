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
import ru.candle.store.orderservice.dictionary.ExceptionCode;
import ru.candle.store.orderservice.dto.request.integration.GetProductsInfoRequest;
import ru.candle.store.orderservice.dto.response.ProductInfo;
import ru.candle.store.orderservice.dto.response.integration.GetProductsInfoResponse;
import ru.candle.store.orderservice.dto.response.integration.GetUserInfoResponse;
import ru.candle.store.orderservice.exception.OrderException;
import ru.candle.store.orderservice.service.impl.IntegrationServiceImpl;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class IntegrationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private IntegrationServiceImpl integrationService;

    @Test
    void whenGetProductInfoByIdsSuccess() throws OrderException {
        GetProductsInfoRequest request = new GetProductsInfoRequest(List.of(1L, 2L));
        HttpHeaders headers = new HttpHeaders();
        headers.set("role", "MANAGER");
        HttpEntity<GetProductsInfoRequest> entity = new HttpEntity<>(request, headers);
        GetProductsInfoResponse expResponse = GetProductsInfoResponse.builder().success(true).productsInfo(List.of(
                new ProductInfo(1L, "image", "title", "description", "subtitle", 1L, "type", "measure", "unit", true),
                new ProductInfo(2L, "image2", "title2", "description2", "subtitle2", 2L, "type2", "measure2", "unit2", false)
        )).build();
        Mockito.when(restTemplate.postForObject("http://product-mplace-service/product/info/get", entity, GetProductsInfoResponse.class)).thenReturn(expResponse);

        GetProductsInfoResponse rs = integrationService.getProductInfoByIds(List.of(1L, 2L), "MANAGER");
        Assertions.assertEquals(expResponse, rs);
    }

    @Test
    void whenGetProductInfoByIdsFail() throws OrderException {
        GetProductsInfoRequest request = new GetProductsInfoRequest(List.of(1L, 2L));
        HttpHeaders headers = new HttpHeaders();
        headers.set("role", "MANAGER");
        HttpEntity<GetProductsInfoRequest> entity = new HttpEntity<>(request, headers);
        Mockito.when(restTemplate.postForObject("http://product-mplace-service/product/info/get", entity,
                GetProductsInfoResponse.class)).thenReturn(null);

        Assertions.assertThrows(OrderException.class, () -> integrationService.getProductInfoByIds(List.of(1L, 2L), "MANAGER"));
    }

    @Test
    void whenGetUserInfoSuccess() throws OrderException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("userId", String.valueOf(1L));
        headers.set("role", "MANAGER");
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        GetUserInfoResponse body = GetUserInfoResponse.builder()
                .success(true)
                .firstName("First")
                .lastName("Last")
                .middleName("Middle")
                .city("City")
                .birthday("1990-01-01")
                .address("Address")
                .build();
        ResponseEntity<GetUserInfoResponse> expResponse = new ResponseEntity<>(body, HttpStatusCode.valueOf(200));
        Mockito.when(restTemplate.exchange("http://profile-service/profile/get", HttpMethod.GET, entity, GetUserInfoResponse.class)).thenReturn(expResponse);

        Assertions.assertEquals(expResponse.getBody(), integrationService.getUserInfo(1L, "MANAGER"));
    }

    @Test
    void whenGetUserInfoFail() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("userId", String.valueOf(1L));
        headers.set("role", "MANAGER");
        GetUserInfoResponse rs = GetUserInfoResponse.builder()
                .success(false)
                .errorCode(ExceptionCode.GET_USER_PROFILE_IS_NULL.getErrorCode())
                .errorText(ExceptionCode.GET_USER_PROFILE_IS_NULL.getErrorText())
                .build();
        ResponseEntity<GetUserInfoResponse> entityRs = new ResponseEntity<>(rs, HttpStatusCode.valueOf(200));
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        Mockito.when(restTemplate.exchange("http://profile-service/profile/get", HttpMethod.GET, entity, GetUserInfoResponse.class)).thenReturn(entityRs);

        Assertions.assertThrows(OrderException.class, () -> integrationService.getUserInfo(1L, "MANAGER"));
    }

}
