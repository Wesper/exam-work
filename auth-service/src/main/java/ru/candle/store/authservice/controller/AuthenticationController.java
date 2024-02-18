package ru.candle.store.authservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.candle.store.authservice.dto.request.GetTokenInfoRequest;
import ru.candle.store.authservice.dto.request.SignInRequest;
import ru.candle.store.authservice.dto.request.SignUpRequest;
import ru.candle.store.authservice.dto.request.ValidateRequest;
import ru.candle.store.authservice.dto.response.GetTokenInfoResponse;
import ru.candle.store.authservice.dto.response.JwtAuthenticationResponse;
import ru.candle.store.authservice.dto.response.ValidateResponse;
import ru.candle.store.authservice.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/signUp")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest rq) {
        return authenticationService.signUp(rq);
    }

    @PostMapping("/signIn")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest rq) {
        return authenticationService.signIn(rq);
    }

    @PostMapping("/validate")
    public ValidateResponse validate(@RequestBody @Valid ValidateRequest rq) {
        return authenticationService.isValid(rq);
    }

    @PostMapping("/token/info/get")
    public GetTokenInfoResponse getTokenInfo(@RequestBody @Valid GetTokenInfoRequest rq) {
        return authenticationService.getTokenInfo(rq);
    }

}
