package ru.candle.store.authservice;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.candle.store.authservice.config.JwtAuthenticationFilter;
import ru.candle.store.authservice.controller.UserManagementController;
import ru.candle.store.authservice.dto.response.ChangePasswordResponse;
import ru.candle.store.authservice.service.AuthenticationService;
import ru.candle.store.authservice.service.UserManagementService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserManagementController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private UserManagementService userManagementService;

    @MockBean
    private JwtAuthenticationFilter filter;

    @Test
    void whenChangePasswordSuccess() throws Exception {
        String requset = "{\"username\": \"userlogin\", \"currentPassword\": \"password1\", \"newPassword\": \"password2\"}";
        Mockito.when(userManagementService.changePassword(Mockito.any())).thenReturn(new ChangePasswordResponse(true, null, null));
        mockMvc.perform(MockMvcRequestBuilders.post("/user/password/change").content(requset).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\": true}"));
    }

    @Test
    void whenChangePasswordFail() throws Exception {
        String requset = "{\"username\": \"user\", \"currentPassword\": \"pass1\"}";
        Mockito.when(authenticationService.getTokenInfo(Mockito.any())).thenThrow(RuntimeException.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/password/change").content(requset).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}