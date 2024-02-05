package ru.candle.store.orderservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.candle.store.orderservice.dto.request.integration.GetProductsInfoRequest;
import ru.candle.store.orderservice.dto.response.integration.GetProductsInfoResponse;
import ru.candle.store.orderservice.dto.response.integration.GetUserInfoResponse;
import ru.candle.store.orderservice.service.IIntegrationService;

import java.util.List;

@Service
public class IntegrationServiceImpl implements IIntegrationService {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public GetProductsInfoResponse getProductInfoByIds(List<Long> productId, String role) {
        GetProductsInfoRequest request = new GetProductsInfoRequest(productId);
        HttpHeaders headers = new HttpHeaders();
        headers.set("role", role);
        HttpEntity<GetProductsInfoRequest> entity = new HttpEntity<>(request, headers);
        GetProductsInfoResponse response = restTemplate.postForObject("http://product-mplace-service/product/info/get", entity, GetProductsInfoResponse.class);
        if (response != null) {
            return response;
        } else {
            throw new RuntimeException("Произошла ошибка при проверке продукта в списке пкупленных у пользователя");
        }
    }

    @Override
    public GetUserInfoResponse getUserInfo(Long userId, String role) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("userId", String.valueOf(userId));
        headers.set("role", role);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<GetUserInfoResponse> response = restTemplate.exchange("http://profile-service/profile/get", HttpMethod.GET, entity, GetUserInfoResponse.class);
        if (response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("Произошла ошибка при проверке продукта в списке пкупленных у пользователя");
        }
    }


}