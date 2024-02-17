package ru.candle.store.ui.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.candle.store.ui.service.IAuthService;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService service;

    @GetMapping(value = "/signUp")
    public String signUp() {
        return "signup";
    }

    @PostMapping(value = "/signUp")
    public String signUp(@RequestBody MultiValueMap<String, String> creds, HttpServletResponse servletResponse, Model model) {
        return service.signUp(creds, servletResponse, model);
    }

    @GetMapping(value = "/signIn")
    public String signIn() {
        return "signin";
    }

    @PostMapping(value = "/signIn")
    public String signIn(@RequestBody MultiValueMap<String, String> creds, HttpServletResponse servletResponse, Model model) {
        return service.signIn(creds, servletResponse, model);
    }

    @GetMapping(value = "/logOut")
    public String logout(HttpServletResponse servletResponse) {
        return service.logout(servletResponse);
    }
}
