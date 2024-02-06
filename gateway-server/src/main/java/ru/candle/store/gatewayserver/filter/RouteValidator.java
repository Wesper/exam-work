package ru.candle.store.gatewayserver.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    /**
     * URI, для которых не нужна авторизация
     */
    public static final List<String> openApiEndpoints = List.of(
            "/auth/signIn",
            "/auth/signUp",
            "/auth/validate",
            "/auth/token/info/get",
            "/product/get",
            "/product/card"
    );

    /**
     * Проверка на необходимость авторизации
     */
    public Predicate<ServerHttpRequest> isSecured = request -> openApiEndpoints.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));
}
