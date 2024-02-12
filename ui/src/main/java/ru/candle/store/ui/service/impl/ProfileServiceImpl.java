package ru.candle.store.ui.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import ru.candle.store.ui.components.Helper;
import ru.candle.store.ui.dto.request.SaveProfileRequest;
import ru.candle.store.ui.dto.response.GetProfileResponse;
import ru.candle.store.ui.dto.response.SaveProfileResponse;
import ru.candle.store.ui.exception.UIException;
import ru.candle.store.ui.service.IProfileService;

@Service
public class ProfileServiceImpl implements IProfileService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Helper helper;

    @Value("${gateway.host}")
    private String gatewayUrl;

    @Override
    public String showProfile(Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        try {
            return showProfileResponse(model, servletRequest);
        } catch (UIException e) {
            return "redirect:../../auth/signIn";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    @Override
    public String saveProfile(SaveProfileRequest request, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        try {
            return saveProfileResponse(request, model, servletRequest);
        } catch (UIException e) {
            return "redirect:../../auth/signIn";
        } catch (Exception e) {
            return helper.exceptionRedirectResolver(e, servletResponse, model);
        }
    }

    private String saveProfileResponse(SaveProfileRequest request, Model model, HttpServletRequest servletRequest) throws UIException {
        HttpEntity<SaveProfileRequest> entity = new HttpEntity<>(request, helper.createAuthHeader(servletRequest));
        ResponseEntity<SaveProfileResponse> response = restTemplate.exchange(gatewayUrl + "/gateway-server/profile/save", HttpMethod.POST, entity, SaveProfileResponse.class);
        if (response.getBody() == null || !response.getBody().isSuccess()) {
            model.addAttribute("errorText", response.getBody().getErrorText());
            return "error";
        }
        model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
        return "redirect:../profile";
    }

    private String showProfileResponse(Model model, HttpServletRequest servletRequest) throws UIException {
        HttpEntity<Void> entity = new HttpEntity<>(helper.createAuthHeader(servletRequest));
        ResponseEntity<GetProfileResponse> response = restTemplate.exchange(gatewayUrl + "/gateway-server/profile/get", HttpMethod.GET, entity, GetProfileResponse.class);
        if (response.getBody() == null || !response.getBody().isSuccess()) {
            model.addAttribute("errorText", response.getBody().getErrorText());
            return "error";
        }
        model.addAttribute("role", helper.getCookiaValueByName(servletRequest, "role"));
        model.addAttribute("firstName", response.getBody().getFirstName());
        model.addAttribute("lastName", response.getBody().getLastName());
        model.addAttribute("middleName", response.getBody().getMiddleName());
        model.addAttribute("city", response.getBody().getCity());
        model.addAttribute("birthday", response.getBody().getBirthday());
        model.addAttribute("address", response.getBody().getAddress());
        return "profile";
    }

}
