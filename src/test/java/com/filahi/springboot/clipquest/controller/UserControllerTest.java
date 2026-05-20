package com.filahi.springboot.clipquest.controller;


import com.filahi.springboot.clipquest.entity.Authority;
import com.filahi.springboot.clipquest.response.UserResponse;
import com.filahi.springboot.clipquest.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private final String BASE_URL = "/api/users";


    @DisplayName("Should get user info")
    @Test
    @WithMockUser
    public void getUserInfoTest() throws Exception {
        UserResponse userResponse = new UserResponse(
                1L,
                "mario",
                "rossi",
                29,
                "mario.rossi@example.com",
                "3874327123",
                "/mock-image-path",
                List.of(new Authority("ROLE_USER"))
        );

        when(userService.getUserInfo()).thenReturn(userResponse);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/info"))
                .andExpect(status().isOk());
    }

    @DisplayName("Should delete user successfully")
    @Test
    @WithMockUser
    public void deleteUserTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL))
                .andExpect(status().isOk());
    }

}
