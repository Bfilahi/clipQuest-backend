package com.filahi.springboot.clipquest.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.filahi.springboot.clipquest.request.LoginRequest;
import com.filahi.springboot.clipquest.request.RegisterRequest;
import com.filahi.springboot.clipquest.service.AuthenticationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationService authenticationService;


    private final String BASE_URL = "/api/auth";



    @DisplayName("Should sign-up correctly")
    @Test
    public void signupTest() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(
                "mario",
                "rossi",
                29,
                "mario.rossi@example.com",
                "Password123!",
                "3847602345"
        );

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/signup")
                        .content(new ObjectMapper().writeValueAsString(registerRequest))
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }

    @DisplayName("Should login successfully")
    @Test
    public void loginTest() throws Exception {
        LoginRequest loginRequest = new LoginRequest("mario.rossi@example.com", "Password123!");

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/login")
                .content(new ObjectMapper().writeValueAsString(loginRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());
    }
}
