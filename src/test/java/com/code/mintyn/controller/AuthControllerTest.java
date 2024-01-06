package com.code.mintyn.controller;

import com.code.mintyn.models.LoginResponseModel;
import com.code.mintyn.models.ResponseMessage;
import com.code.mintyn.models.dto.LoginDTO;
import com.code.mintyn.models.dto.UserCreateDTO;
import com.code.mintyn.service.abstracts.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void login() throws Exception {
        LoginDTO loginDTO = new LoginDTO("user", "password");
        LoginResponseModel expectedResponse = new LoginResponseModel("user", "token", null);

        given(authenticationService.login(loginDTO)).willReturn(expectedResponse);

        mockMvc.perform(post("/v1/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    void signUp() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO("newUser", "password");
        ResponseMessage expectedResponse = new ResponseMessage("User Signed Up Successfully");

        given(authenticationService.signUp(userCreateDTO)).willReturn(expectedResponse);

        mockMvc.perform(post("/v1/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }
}
