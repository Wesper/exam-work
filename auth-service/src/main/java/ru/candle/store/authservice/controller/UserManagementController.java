package ru.candle.store.authservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.candle.store.authservice.dto.request.ChangePasswordRequest;
import ru.candle.store.authservice.dto.request.ChangeRoleRequest;
import ru.candle.store.authservice.dto.request.GetUserByIdRequest;
import ru.candle.store.authservice.dto.request.GetUserRequest;
import ru.candle.store.authservice.dto.response.ChangePasswordResponse;
import ru.candle.store.authservice.dto.response.ChangeRoleResponse;
import ru.candle.store.authservice.dto.response.GetUserResponse;
import ru.candle.store.authservice.service.UserManagementService;

@RestController
@RequestMapping("/user")
public class UserManagementController {

    @Autowired
    private UserManagementService service;

    @PreAuthorize("#role == 'ADMIN' || #role == 'MANAGER'")
    @PostMapping("/get")
    public GetUserResponse getUser(@RequestBody @Valid GetUserRequest rq, @RequestHeader("role") String role) {
        return service.getUser(rq);
    }

    @PreAuthorize("#role == 'MANAGER' || #role == 'ADMIN'")
    @PostMapping("/id/get")
    public GetUserResponse getUserById(@RequestBody @Valid GetUserByIdRequest rq, @RequestHeader("role") String role) {
        return service.getUserById(rq);
    }

    @PostMapping("/password/change")
    public ChangePasswordResponse changePassword(@RequestBody @Valid ChangePasswordRequest rq) {
        return service.changePassword(rq);
    }

    @PreAuthorize("#role == 'ADMIN'")
    @PostMapping("/role/change")
    public ChangeRoleResponse changeRole(@RequestHeader("role") String role, @RequestBody @Valid ChangeRoleRequest rq) {
        return service.changeRole(rq);
    }
}
