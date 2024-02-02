package ru.candle.store.authservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.candle.store.authservice.dictionary.Role;
import ru.candle.store.authservice.entity.UserEntity;
import ru.candle.store.authservice.repository.UserRepository;
import ru.candle.store.authservice.service.UserService;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    @Test
    void createUserWhenItNotExist() {
        UserEntity expectedUser = new UserEntity(
                "One",
                "OnePass",
                "one@one.ru",
                Role.ROLE_USER
        );
        Mockito.when(repository.save(Mockito.any())).thenReturn(expectedUser);

        UserEntity actualUser = service.create(expectedUser);
        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedUser.getUsername(), actualUser.getUsername()),
                () -> Assertions.assertEquals(expectedUser.getPassword(), actualUser.getPassword()),
                () -> Assertions.assertEquals(expectedUser.getEmail(), actualUser.getEmail()),
                () -> Assertions.assertEquals(expectedUser.getRole().name(), actualUser.getRole().name())
        );
        Mockito.verify(repository, Mockito.times(1)).save(expectedUser);
    }

    @Test
    void createUserWhenUsernameAlreadyExist() {
        UserEntity user = new UserEntity(
                "One",
                "OnePass",
                "one@one.ru",
                Role.ROLE_USER
        );
        Mockito.when(repository.existsByUsername(Mockito.any())).thenReturn(true);

        Assertions.assertThrows(RuntimeException.class, () -> service.create(user));
        Mockito.verify(repository, Mockito.times(0)).save(user);
    }

    @Test
    void createUserWhenEmailAlreadyExist() {
        UserEntity user = new UserEntity(
                "One",
                "OnePass",
                "one@one.ru",
                Role.ROLE_USER
        );
        Mockito.when(repository.existsByEmail(Mockito.any())).thenReturn(true);

        Assertions.assertThrows(RuntimeException.class, () -> service.create(user));
        Mockito.verify(repository, Mockito.times(0)).save(user);
    }

    @Test
    void changeUserPasswordSuccess() {
        Mockito.when(repository.updatePasswordByUsername(Mockito.any(), Mockito.any())).thenReturn(1);

        Assertions.assertTrue(service.changePassword("pass", "login"));
    }

    @Test
    void changeUserPasswordFail() {
        Mockito.when(repository.updatePasswordByUsername(Mockito.any(), Mockito.any())).thenReturn(0);

        Assertions.assertThrows(RuntimeException.class, () -> service.changePassword("pass", "login"));
    }

}
