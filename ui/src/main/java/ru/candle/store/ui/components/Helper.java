package ru.candle.store.ui.components;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.client.HttpClientErrorException;
import ru.candle.store.ui.dictionary.ExceptionCode;
import ru.candle.store.ui.exception.UIException;

import java.util.Arrays;

@Component
public class Helper {

    private static final String AUTH_PREFIX = "Bearer ";

    public HttpHeaders createAuthHeader(HttpServletRequest servletRequest) throws UIException {
        if (servletRequest.getCookies() == null) {
            throw new UIException(ExceptionCode.AUTHORIZATION_HEADER_EMPTY, "Необходимо авторизоваться");
        }
        String token = Arrays.stream(servletRequest.getCookies()).filter(c -> c.getName().equals("Authorization"))
                .findFirst().orElseThrow(() -> new UIException(ExceptionCode.AUTHORIZATION_HEADER_EMPTY, "Необходимо авторизоваться")).getValue();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", AUTH_PREFIX + token);
        return headers;
    }

    public String exceptionRedirectResolver(Exception e, HttpServletResponse servletResponse, Model model) {
        if (((HttpClientErrorException) e).getStatusCode().value() == 403) {
            Cookie cookie = new Cookie("Authorization", "");
            cookie.setDomain("localhost");
            cookie.setPath("/");
            Cookie roleCookie = new Cookie("role", "");
            roleCookie.setDomain("localhost");
            roleCookie.setPath("/");
            servletResponse.addCookie(cookie);
            servletResponse.addCookie(roleCookie);
            return "redirect:/auth/signIn";
        }
        if (((HttpClientErrorException) e).getStatusCode().value() == 401) {
            model.addAttribute("errorText", "Отсутствуют права на выполнение операции");
            return "error";
        }
        return "redirect:../../error";
    }

    public String getCookiaValueByName(HttpServletRequest servletRequest, String role) {
        if (servletRequest.getCookies() != null) {
            return Arrays.stream(servletRequest.getCookies()).filter(cookie -> cookie.getName().equals(role))
                    .findFirst().orElse(new Cookie("role", "")).getValue();
        } else {
            return "";
        }

    }
}
