package ru.candle.store.authservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.candle.store.authservice.dto.request.ChangePasswordRequest;
import ru.candle.store.authservice.dto.request.ChangeRoleRequest;
import ru.candle.store.authservice.dto.request.GetUserByIdRequest;
import ru.candle.store.authservice.dto.request.GetUserRequest;
import ru.candle.store.authservice.dto.response.ChangePasswordResponse;
import ru.candle.store.authservice.dto.response.ChangeRoleResponse;
import ru.candle.store.authservice.dto.response.GetUserResponse;
import ru.candle.store.authservice.entity.UserEntity;

@Service
public class UserManagementService {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Изменение пароля пользователя
     *
     * @param request логин, текущий пароль, новый пароль
     * @return результат смены пароля
     */
    public ChangePasswordResponse changePassword(ChangePasswordRequest request) {
        boolean result;
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getCurrentPassword()
            ));
            result = userService.changePassword(passwordEncoder.encode(request.getNewPassword()), request.getUsername());
        } catch (AuthenticationException e) {
            return ChangePasswordResponse.builder()
                    .success(false)
                    .errorCode("CHANGE_PASSWORD_FAILED")
                    .errorText("Не удалось изменить пароль")
                    .build();
        } catch (Exception e) {
            return ChangePasswordResponse.builder()
                    .success(false)
                    .errorCode("UNKNOWN_EXCEPTION")
                    .errorText("Произошла непредвиденная ошибка")
                    .build();
        }
        return ChangePasswordResponse.builder().success(result).build();
    }

    /**
     * Изменение роли пользователя
     *
     * @param request логин, текущий пароль, новый пароль
     * @return результат смены пароля
     */
    public ChangeRoleResponse changeRole(ChangeRoleRequest request) {
        boolean result;
        try {
            result = userService.changeRole(request.getUserName(), String.valueOf(request.getNewRole()));
        } catch (Exception e) {
            return ChangeRoleResponse.builder()
                    .success(false)
                    .errorCode("UNKNOWN_EXCEPTION")
                    .errorText("Произошла непредвиденная ошибка")
                    .build();
        }
        return ChangeRoleResponse.builder().success(result).build();
    }

    /**
     * Плучение информации о пользователе
     *
     * @param request имя пользователя
     * @return информация о пользователе
     */
    public GetUserResponse getUser(GetUserRequest request) {
        try {
            UserEntity user = userService.getByUsername(request.getUserName());
            return GetUserResponse.builder()
                    .success(true)
                    .userId(String.valueOf(user.getId()))
                    .userName(user.getUsername())
                    .email(user.getEmail())
                    .role(String.valueOf(user.getRole()))
                    .build();
        } catch (UsernameNotFoundException e) {
            return GetUserResponse.builder()
                    .success(false)
                    .errorCode("USER_NOT_FOUND")
                    .errorText("Пользователь не найден")
                    .build();
        } catch (Exception e) {
            return GetUserResponse.builder()
                    .success(false)
                    .errorCode("UNKNOWN_EXCEPTION")
                    .errorText("Произошла непредвиденная ошибка")
                    .build();
        }
    }

    /**
     * Плучение информации о пользователе
     *
     * @param request имя пользователя
     * @return информация о пользователе
     */
    public GetUserResponse getUserById(GetUserByIdRequest request) {
        try {
            UserEntity user = userService.getByUserId(request.getUserId());
            return GetUserResponse.builder()
                    .success(true)
                    .userId(String.valueOf(user.getId()))
                    .userName(user.getUsername())
                    .email(user.getEmail())
                    .role(String.valueOf(user.getRole()))
                    .build();
        } catch (UsernameNotFoundException e) {
            return GetUserResponse.builder()
                    .success(false)
                    .errorCode("USER_NOT_FOUND")
                    .errorText("Пользователь не найден")
                    .build();
        } catch (Exception e) {
            return GetUserResponse.builder()
                    .success(false)
                    .errorCode("UNKNOWN_EXCEPTION")
                    .errorText("Произошла непредвиденная ошибка")
                    .build();
        }
    }
}
