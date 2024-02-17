package ru.candle.store.ui.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.candle.store.ui.components.Helper;
import ru.candle.store.ui.dto.request.SignInRequest;
import ru.candle.store.ui.dto.request.SignUpRequest;
import ru.candle.store.ui.dto.response.SignInUpResponse;
import ru.candle.store.ui.service.IAuthService;

import java.util.Base64;

@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Helper helper;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${gateway.host}")
    private String gatewayUrl;

    @Override
    public String signUp(MultiValueMap<String, String> creds, HttpServletResponse servletResponse, Model model) {
        SignUpRequest request = SignUpRequest.builder()
                .password(creds.get("password").get(0))
                .username(creds.get("username").get(0))
                .email(creds.get("email").get(0))
                .build();
        try {
            SignInUpResponse response = restTemplate.postForObject(gatewayUrl + "/auth/signUp", request, SignInUpResponse.class);
            Cookie authCookie = new Cookie("AUTHORIZATION", response.getToken());
            authCookie.setDomain("localhost");
            authCookie.setPath("/");
            servletResponse.addCookie(authCookie);
            Cookie roleCookie = new Cookie("role", extractRoleFromToken(response.getToken()));
            roleCookie.setDomain("localhost");
            roleCookie.setPath("/");
            servletResponse.addCookie(roleCookie);
            return "redirect:/home";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    @Override
    public String signIn(MultiValueMap<String, String> creds, HttpServletResponse servletResponse, Model model) {
        SignInRequest request = SignInRequest.builder()
                .username(creds.get("username").get(0))
                .password(creds.get("password").get(0))
                .build();
        try {
            SignInUpResponse response = restTemplate.postForObject(gatewayUrl + "/auth/signIn", request, SignInUpResponse.class);
            Cookie authCookie = new Cookie("AUTHORIZATION", response.getToken());
            authCookie.setDomain("localhost");
            authCookie.setPath("/");
            servletResponse.addCookie(authCookie);
            Cookie roleCookie = new Cookie("role", extractRoleFromToken(response.getToken()));
            roleCookie.setDomain("localhost");
            roleCookie.setPath("/");
            servletResponse.addCookie(roleCookie);
            return "redirect:/home";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    @Override
    public String logout(HttpServletResponse servletResponse) {
        Cookie cookie = new Cookie("AUTHORIZATION", "");
        cookie.setDomain("localhost");
        cookie.setPath("/");
        servletResponse.addCookie(cookie);
        Cookie roleCookie = new Cookie("role", "");
        roleCookie.setDomain("localhost");
        roleCookie.setPath("/");
        servletResponse.addCookie(roleCookie);
        return "redirect:/auth/signIn";
    }

    private String extractRoleFromToken(String token) throws JsonProcessingException {
        String[] tokenArray = token.split("\\.");
        String decoded = new String(Base64.getDecoder().decode(tokenArray[1]));
        return objectMapper.readValue(decoded, JsonNode.class).get("role").asText();
    }
}
