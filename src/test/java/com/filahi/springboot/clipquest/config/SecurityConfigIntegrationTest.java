package com.filahi.springboot.clipquest.config;


import com.filahi.springboot.clipquest.entity.User;
import com.filahi.springboot.clipquest.repository.UserRepository;
import com.filahi.springboot.clipquest.service.impl.JwtServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class SecurityConfigIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @Value("${spring.jwt.secret}")
    private String jwtSecret;


    @DisplayName("Should permit access to public endpoints without authentication")
    @Test
    public void shouldPermitAccessToPublicEndpointsWithoutAuthentication() throws Exception {
        String[] publicEndpoints = {
                "/api/auth/login",
                "/api/videos",
                "/api/videos/video",
                "/api/videos/1/view",
                "/api/comments/video",

                "/swagger-ui",
                "/v3/api-docs",
                "/swagger/resources",
                "/webjars",
                "/docs"
        };

        for(String publicEndpoint : publicEndpoints) {
            mockMvc.perform(MockMvcRequestBuilders.post(publicEndpoint))
                    .andExpect(status().is(not(HttpStatus.UNAUTHORIZED.value())));
        }
    }

    @DisplayName("Should return 401 when accessing protected endpoint without authentication")
    @Test
    public void shouldReturn401WhenAccessingProtectedEndpointWithoutAuthentication() throws Exception {
        String protectedEndpoint = "/api/admin/clips";

        mockMvc.perform(MockMvcRequestBuilders.get(protectedEndpoint))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Should permit access to protected endpoint with valid roles")
    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldPermitAccessToProtectedEndpointWithValidRoles() throws Exception {
        String protectedEndpoint = "/api/admin/clips";

        mockMvc.perform(MockMvcRequestBuilders.get(protectedEndpoint))
                .andExpect(status().is(not(HttpStatus.UNAUTHORIZED.value())));
    }

    @DisplayName("Should permit access to protected endpoint with valid token")
    @Test
    public void shouldPermitAccessToProtectedEndpointWithValidToken() throws Exception {
        User user = new User();
        user.setFirstName("mario");
        user.setLastName("rossi");
        user.setEmail("mario.rossi@example.com");
        user.setAuthorities(List.of());

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        JwtServiceImpl jwtService = new JwtServiceImpl();
        ReflectionTestUtils.setField(jwtService, "SECRET", jwtSecret);
        ReflectionTestUtils.setField(jwtService, "JWT_EXPIRATION", 86400000L);

        String token = jwtService.generateToken(new HashMap<>(), user);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/clips")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is(not(HttpStatus.UNAUTHORIZED.value())))
                .andExpect(status().is(not(HttpStatus.FORBIDDEN.value())));
    }

    @DisplayName("Should succeed on POST request without CSRF token")
    @Test
    public void shouldSucceedOnPostRequestWithoutCsrfToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/clips/add"))
                .andExpect(status().is(not(HttpStatus.FORBIDDEN.value())));
    }

    @DisplayName("Should not create session after successful request")
    @Test
    public void shouldNotCreateSessionAfterSuccessfulRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/clips/add"))
                .andExpect(request().sessionAttributeDoesNotExist("JSESSIONID"));
    }
}
