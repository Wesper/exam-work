package ru.candle.store.authservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.candle.store.authservice.dictionary.Role;
import ru.candle.store.authservice.dto.request.*;
import ru.candle.store.authservice.dto.response.*;
import ru.candle.store.authservice.entity.EmailDetails;
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
    @Autowired
    private EmailService emailService;

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
                Role.USER
        );

        user = userService.create(user);

        String jwt = jwtService.generateToken(user);
        EmailDetails email = new EmailDetails();
                email.setRecipient(user.getEmail());
                email.setMsgBody("Поздравляем с успешной регистрацией " + user.getUsername());
                email.setSubject("Поздравляем с регистрацией");
        emailService.sendEmail(email);
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

}
