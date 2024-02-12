package ru.candle.store.ui.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;

public interface IAuthService {

    String signUp(MultiValueMap<String, String> creds, HttpServletResponse servletResponse, Model model);
    String signIn(MultiValueMap<String, String> creds, HttpServletResponse servletResponse, Model model);
    String logout(HttpServletResponse servletResponse);
}
