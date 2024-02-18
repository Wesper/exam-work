package ru.candle.store.gatewayserver.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
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
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${server.port}")
    private String port;

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
                log.info(requestLog(exchange.getRequest()));
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    log.error("Заголовок авторизации отсутствует");
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Заголовок авторизации отсутствует");
                }

                String authToken = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
                if (StringUtils.isNotBlank(authToken) && authToken.startsWith(AUTH_PREFIX)) {
                    authToken = authToken.substring(AUTH_PREFIX.length());
                    log.info("Токен авторизации " + authToken);
                }
                try {
                    ValidateResponse validateResponse = restTemplate.postForObject(getUrlDestService() + "/auth/validate", new ValidateRequest(authToken), ValidateResponse.class);
                    if (!validateResponse.isSuccess()) {
                        log.error("Токен не валидный");
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Токен не валидный");
                    }
                    GetTokenInfoResponse getTokenInfoResponse = restTemplate.postForObject(getUrlDestService() + "/auth/token/info/get", new GetTokenInfoRequest(authToken), GetTokenInfoResponse.class);
                    userInfoHeaders = exchange.getRequest()
                            .mutate()
                            .header("userId", String.valueOf(getTokenInfoResponse.getUserId()))
                            .header("username", getTokenInfoResponse.getUsername())
                            .header("email", getTokenInfoResponse.getEmail())
                            .header("role", getTokenInfoResponse.getRole())
                            .build();
                    log.info(responseLog(userInfoHeaders));
                } catch (ResponseStatusException e) {
                    log.error("Произошла ошибка при валидации токена");
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ошибка при создании токена", e);
                } catch (Exception e) {
                    log.error("Произошла ошибка при создании токена");
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка при создании токена", e);
                }
            }
            log.info("Запрос " + exchange.getRequest().getURI() + " не нуждается в авторизации");
            return chain.filter(exchange.mutate().request(userInfoHeaders).build());
        }));
    }

    private String getUrlDestService() {
        ServiceInstance serviceInstance = discoveryClient.getInstances("auth-service").get(0);
        return "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort();
    }

    private String requestLog(ServerHttpRequest request) {
        return "На входе в фильтр \n" +
                "METHOD " + request.getMethod().name() + "\n" +
                "URI " + request.getURI() + "\n" +
                "HEADERS " + request.getHeaders() + "\n" +
                "COOKIES " + request.getCookies() + "\n";
    }

    private String responseLog(ServerHttpRequest request) {
        return "На выходе из фильтра \n" +
                "METHOD " + request.getMethod().name() + "\n" +
                "URI " + request.getURI() + "\n" +
                "HEADERS " + request.getHeaders() + "\n" +
                "COOKIES " + request.getCookies() + "\n";
    }
}
