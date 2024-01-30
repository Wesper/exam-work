package ru.candle.store.authservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.candle.store.authservice.dictionary.Role;
import ru.candle.store.authservice.entity.UserEntity;
import ru.candle.store.authservice.repository.UserRepository;
import ru.candle.store.authservice.service.JwtService;
import ru.candle.store.authservice.service.UserService;


@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @InjectMocks
    private JwtService service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "jwtSigningKey", "ErzJNc2sOWV4Eo5zRSyqTy2cWWf2NOGf+PZrhl90mxjdkvMilNARpee7LTS/bK8YYAIdeQmVcg1TcFag7ux+S6nF0Yvst59EyQ4+CHyr/W46//YNkpq72v8z/UhrNwPmIFX8iqM+vgP/Y/e/0KAhphLxdbDCclS1ZAiZ4ovgZI61skp9hkQT/4tAlMBWzT2+Yiuf5trbjW15970J7OimOP9VIRkAvZ9EMK5Jn1QERk0StDqes9Lu/Z7DuWqIbKJ54i3GOF2ntlEbZxhvHYPXuvWNHXp8ukKYVuEqQk8zChIT/QXOSdwXz8wLM7MQtDQs9qjHca6jM1oBTd5bwgb4M/H7XsPSb5fGcRT8g8mmI/Q=");
        ReflectionTestUtils.setField(service, "jwtSessionTime", 1800000L);
    }

    @Test
    void WhenExtractUsernameFromTokenSuccess() {
        UserEntity user = new UserEntity(
                "One",
                "OnePass",
                "one@one.ru",
                Role.ROLE_USER
        );
        String token = service.generateToken(user);
        Assertions.assertEquals("One", service.extractUsername(token));
    }

    @Test
    void WhenExtractUsernameFromTokenFail() {
        String token = "";
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.extractUsername(token));
    }

    @Test
    void WhenTokenValidSuccess() {
        UserEntity user = new UserEntity(
                "One",
                "OnePass",
                "one@one.ru",
                Role.ROLE_USER
        );
        String token = service.generateToken(user);

        Assertions.assertTrue(service.isTokenValid(token));
    }

    @Test
    void WHenTokenNotValid() {
        String token = "teststring";

        Assertions.assertFalse(service.isTokenValid(token));
    }

    @Test
    void WhenClaimsExtractCorrect() {
        UserEntity expectedUser = new UserEntity(
                "One",
                "OnePass",
                "one@one.ru",
                Role.ROLE_USER
        );

        String token = service.generateToken(expectedUser);
        UserEntity actualUser = service.getClaims(token);

        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedUser.getUsername(), actualUser.getUsername()),
                () -> Assertions.assertEquals(null, actualUser.getPassword()),
                () -> Assertions.assertEquals(expectedUser.getEmail(), actualUser.getEmail()),
                () -> Assertions.assertEquals(expectedUser.getRole().name(), actualUser.getRole().name())
        );
    }
}