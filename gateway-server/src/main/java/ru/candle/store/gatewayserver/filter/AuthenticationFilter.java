package ru.candle.store.gatewayserver.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.candle.store.gatewayserver.dto.request.GetTokenInfoRequest;
import ru.candle.store.gatewayserver.dto.request.ValidateRequest;
import ru.candle.store.gatewayserver.dto.response.GetTokenInfoResponse;
import ru.candle.store.gatewayserver.dto.response.ValidateResponse;

import java.util.Objects;

@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    RestTemplate restTemplate;

    private static final String AUTH_PREFIX = "Bearer ";

    public AuthenticationFilter() {
        super(Config.class);
    }

    public static class Config {
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
            ServerHttpRequest userInfoHeaders = null;
            if (validator.isSecured.test(exchange.getRequest())) {
                log.info("Запрос " + exchange.getRequest());
                if (exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    log.error("Заголовок авторизации отсутствует");
                    throw new RuntimeException("Заголовок авторизации отсутствует");
                }

                String authToken = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
                if (StringUtils.isNotBlank(authToken) && authToken.startsWith(AUTH_PREFIX)) {
                    authToken = authToken.substring(AUTH_PREFIX.length());
                    log.info("Токен авторизации " + authToken);
                }
                try {
                    ValidateResponse validateResponse = restTemplate.postForObject("http://auth-service/auth/validate", new ValidateRequest(authToken), ValidateResponse.class);
                    if (!validateResponse.isSuccess()) {
                        log.error("Токен не валидный");
                        throw new RuntimeException("Токен не валидный");
                    }
                    GetTokenInfoResponse getTokenInfoResponse = restTemplate.postForObject("http://auth-service/auth/token/info/get", new GetTokenInfoRequest(authToken), GetTokenInfoResponse.class);
                    userInfoHeaders = exchange.getRequest()
                            .mutate()
                            .header("userId", String.valueOf(getTokenInfoResponse.getUserId()))
                            .header("username", getTokenInfoResponse.getUsername())
                            .header("email", getTokenInfoResponse.getEmail())
                            .header("role", getTokenInfoResponse.getRole())
                            .build();
                    log.info("Атрибуты сессии пользователя " + userInfoHeaders);
                } catch (Exception e) {
                    log.error("Произошла ошибка при создании токена");
                    throw new RuntimeException("Ошибка при создании токена");
                }
            }

            return chain.filter(exchange.mutate().request(userInfoHeaders).build());
        }));
    }
}
