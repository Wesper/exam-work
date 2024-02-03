package ru.candle.store.profileservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.candle.store.profileservice.dto.request.SaveProfileRequest;
import ru.candle.store.profileservice.dto.response.GetProfileResponse;
import ru.candle.store.profileservice.dto.response.SaveProfileResponse;
import ru.candle.store.profileservice.service.IProfileService;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    IProfileService profileService;

    @PreAuthorize("#role == 'USER'")
    @GetMapping(value = "/get")
    public GetProfileResponse getUserProfile(@RequestHeader("role") String role, @RequestHeader("userId") Long userId) {
        return profileService.getUserProfile(userId);
    }

    @PreAuthorize("#role == 'USER'")
    @PostMapping(value = "/save")
    public SaveProfileResponse saveUserProfile(@RequestBody @Valid SaveProfileRequest rq, @RequestHeader("role") String role, @RequestHeader("userId") Long userId) {
        return profileService.saveUserProfile(rq, userId);
    }
}
