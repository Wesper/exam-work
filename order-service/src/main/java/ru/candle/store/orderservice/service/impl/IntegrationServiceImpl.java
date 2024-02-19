package ru.candle.store.orderservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.candle.store.orderservice.dictionary.ExceptionCode;
import ru.candle.store.orderservice.dto.request.integration.GetProductsInfoRequest;
import ru.candle.store.orderservice.dto.request.integration.GetUserAuthRequest;
import ru.candle.store.orderservice.dto.response.integration.GetProductsInfoResponse;
import ru.candle.store.orderservice.dto.response.integration.GetUserAuthResponse;
import ru.candle.store.orderservice.dto.response.integration.GetUserInfoResponse;
import ru.candle.store.orderservice.exception.OrderException;
import ru.candle.store.orderservice.service.IIntegrationService;

import java.util.List;

@Slf4j
@Service
public class IntegrationServiceImpl implements IIntegrationService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public GetProductsInfoResponse getProductInfoByIds(List<Long> productId, String role) throws OrderException {
        GetProductsInfoRequest request = new GetProductsInfoRequest(productId);
        HttpHeaders headers = new HttpHeaders();
        headers.set("role", role);
        HttpEntity<GetProductsInfoRequest> entity = new HttpEntity<>(request, headers);
        GetProductsInfoResponse response = restTemplate.postForObject("http://product-mplace-service/product/info/get", entity, GetProductsInfoResponse.class);
        if (response != null && response.getSuccess()) {
            log.info(response.toString());
            return response;
        } else {
            log.error("Ответ пуст");
            throw new OrderException(ExceptionCode.GET_PRODUCTS_INFO_IS_NULL, "Произошла ошибка при проверке продукта в списке купленных у пользователя");
        }
    }

    @Override
    public GetUserInfoResponse getUserInfo(Long userId, String role) throws OrderException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("userId", String.valueOf(userId));
        headers.set("role", role);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<GetUserInfoResponse> response = restTemplate.exchange("http://profile-service/profile/get", HttpMethod.GET, entity, GetUserInfoResponse.class);
        if (response.getBody() != null && response.getBody().getSuccess()) {
            log.info(response.getBody().toString());
            return response.getBody();
        } else {
            log.error(response.getBody().toString());
            throw new OrderException(ExceptionCode.GET_USER_PROFILE_IS_NULL, "Произошла ошибка при получении профиля клиента");
        }
    }

    @Override
    public GetUserAuthResponse getUserAuth(Long userId, String role) throws OrderException {
        GetUserAuthRequest request = new GetUserAuthRequest(String.valueOf(userId));
        HttpHeaders headers = new HttpHeaders();
        headers.set("role", role);
        HttpEntity<GetUserAuthRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<GetUserAuthResponse> response = restTemplate.exchange("http://auth-service/user/id/get", HttpMethod.POST, entity, GetUserAuthResponse.class);
        if (response.getBody() != null && response.getBody().isSuccess()) {
            log.info(response.getBody().toString());
            return response.getBody();
        } else {
            log.error(response.getBody().toString());
            throw new OrderException(ExceptionCode.GET_USER_PROFILE_IS_NULL, "Произошла ошибка при получении профиля клиента");
        }
    }


}