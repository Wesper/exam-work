package ru.candle.store.authservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.candle.store.authservice.config.JwtAuthenticationFilter;
import ru.candle.store.authservice.controller.AuthenticationController;
import ru.candle.store.authservice.dto.request.GetTokenInfoRequest;
import ru.candle.store.authservice.dto.response.ChangePasswordResponse;
import ru.candle.store.authservice.dto.response.GetTokenInfoResponse;
import ru.candle.store.authservice.dto.response.JwtAuthenticationResponse;
import ru.candle.store.authservice.dto.response.ValidateResponse;
import ru.candle.store.authservice.service.AuthenticationService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtAuthenticationFilter filter;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void WhenSignUpSuccess() throws Exception {
        String requset = "{\"username\": \"one\", \"password\":  \"onepass\", \"email\": \"one@one.ru\"}";
        Mockito.when(authenticationService.signUp(Mockito.any())).thenReturn(new JwtAuthenticationResponse("token"));
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signUp").content(requset).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\": \"token\"}"));
    }

    @Test
    void WhenSignUpFail() throws Exception {
        String requset = "{\"username\": \"one\", \"password\":  \"onepass\"}";
        Mockito.when(authenticationService.signUp(Mockito.any())).thenReturn(new JwtAuthenticationResponse("token"));
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signUp").content(requset).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void WhenSignInSuccess() throws Exception {
        String requset = "{\"username\": \"one\", \"password\":  \"onepass\"}";
        Mockito.when(authenticationService.signIn(Mockito.any())).thenReturn(new JwtAuthenticationResponse("token"));
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signIn").content(requset).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\": \"token\"}"));
    }

    @Test
    void WhenSignInFail() throws Exception {
        String requset = "{\"username\": \"one\"}";
        Mockito.when(authenticationService.signIn(Mockito.any())).thenReturn(new JwtAuthenticationResponse("token"));
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signIn").content(requset).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void WhenValidateSuccess() throws Exception {
        String requset = "{\"token\": \"token\"}";
        Mockito.when(authenticationService.isValid(Mockito.any())).thenReturn(new ValidateResponse(true));
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/validate").content(requset).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\": true}"));
    }

    @Test
    void WhenValidateFail() throws Exception {
        String requset = "{\"token\": \"\"}";
        Mockito.when(authenticationService.isValid(Mockito.any())).thenReturn(new ValidateResponse(true));
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/validate").content(requset).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void WhenGetTokenInfoSuccess() throws Exception {
        String requset = "{\"token\": \"token\"}";
        Mockito.when(authenticationService.getTokenInfo(Mockito.any())).thenReturn(new GetTokenInfoResponse(1L ,"one", "one@one.ru", "ROLE_USER"));
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/token/info/get").content(requset).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"userId\": 1, \"username\": \"one\", \"email\": \"one@one.ru\", \"role\": \"ROLE_USER\"}"));
    }

    @Test
    void WhenGetTokenInfoFail() throws Exception {
        String requset = "{\"token\": \"\"}";
        Mockito.when(authenticationService.getTokenInfo(Mockito.any())).thenThrow(RuntimeException.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/token/info/get").content(requset).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void WhenChangePasswordSuccess() throws Exception {
        String requset = "{\"username\": \"userlogin\", \"currentPassword\": \"password1\", \"newPassword\": \"password2\"}";
        Mockito.when(authenticationService.changePassword(Mockito.any())).thenReturn(new ChangePasswordResponse(true));
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/password/change").content(requset).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\": true}"));
    }

    @Test
    void WhenChangePasswordFail() throws Exception {
        String requset = "{\"username\": \"user\", \"currentPassword\": \"pass1\"}";
        Mockito.when(authenticationService.getTokenInfo(Mockito.any())).thenThrow(RuntimeException.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/password/change").content(requset).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}