package ru.candle.store.authservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.candle.store.authservice.dictionary.Role;
import ru.candle.store.authservice.dto.request.*;
import ru.candle.store.authservice.dto.response.ChangePasswordResponse;
import ru.candle.store.authservice.dto.response.GetTokenInfoResponse;
import ru.candle.store.authservice.dto.response.JwtAuthenticationResponse;
import ru.candle.store.authservice.dto.response.ValidateResponse;
import ru.candle.store.authservice.entity.UserEntity;

@Service
public class AuthenticationService {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        UserEntity user = new UserEntity(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getEmail(),
                Role.ROLE_USER
        );

        user = userService.create(user);

        String jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        UserDetails user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        String jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * Валидация токена пользователя
     *
     * @param request токен пользователя
     * @return статус валидации
     */
    public ValidateResponse isValid(ValidateRequest request) {
        boolean jwt = jwtService.isTokenValid(request.getToken());
        return new ValidateResponse(jwt);
    }

    /**
     * Получение информации из токена пользователя
     *
     * @param request токен пользователя
     * @return параметры из токена
     */
    public GetTokenInfoResponse getTokenInfo(GetTokenInfoRequest request) {
        UserEntity userEntity = jwtService.getClaims(request.getToken());
        return new GetTokenInfoResponse(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getRole().name()
        );
    }

    /**
     * Изменение пароля пользователя
     *
     * @param request логин, текущий пароль, новый пароль
     * @return результат смены пароля
     */
    public ChangePasswordResponse changePassword(ChangePasswordRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getCurrentPassword()
        ));

        boolean result = userService.changePassword(passwordEncoder.encode(request.getNewPassword()), request.getUsername());
        return new ChangePasswordResponse(result);
    }
}
