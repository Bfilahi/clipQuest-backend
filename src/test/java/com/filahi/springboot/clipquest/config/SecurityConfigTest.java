package com.filahi.springboot.clipquest.config;


import com.filahi.springboot.clipquest.entity.Authority;
import com.filahi.springboot.clipquest.entity.User;
import com.filahi.springboot.clipquest.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SecurityConfigTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PrintWriter printWriter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException authenticationException;

    @InjectMocks
    private SecurityConfig securityConfig;


    private final String FRONTEND_URL = "http://localhost:4200";


    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(securityConfig, "frontendUrl", FRONTEND_URL);
    }


    @DisplayName("Should return user details when user is found")
    @Test
    public void shouldReturnUserDetailsWhenUserIsFound() {
        User user =  new User();
        user.setFirstName("mario");
        user.setLastName("rossi");
        user.setEmail("mario.rossi@example.com");
        user.setPassword("password123");
        user.setAuthorities(List.of(new Authority("ROLE_USER")));

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserDetailsService userDetailsService = securityConfig.userDetailsService();
        UserDetails result = userDetailsService.loadUserByUsername(user.getEmail());

        assertEquals(user.getEmail(), result.getUsername());
    }

    @DisplayName("Should throw UsernameNotFoundException when user is not found")
    @Test
    public void shouldThrowUsernameNotFoundExceptionWhenUserIsNotFound() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        UserDetailsService userDetailsService = securityConfig.userDetailsService();

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("mario.rossi@example.com"));
    }

    @DisplayName("Should set response status to 401 when authentication fails (authenticationEntryPoint())")
    @Test
    public void shouldSetResponseStatusTo401WhenAuthenticationFails() throws IOException, ServletException {
        when(response.getWriter()).thenReturn(printWriter);

        AuthenticationEntryPoint authenticationEntryPoint = securityConfig.authenticationEntryPoint();
        authenticationEntryPoint.commence(request, response, authenticationException);

        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Should set content type to application/json when authentication fails")
    @Test
    public void shouldSetContentTypeToApplicationJsonWhenAuthenticationFails() throws IOException, ServletException {
        when(response.getWriter()).thenReturn(printWriter);

        AuthenticationEntryPoint authenticationEntryPoint = securityConfig.authenticationEntryPoint();
        authenticationEntryPoint.commence(request, response, authenticationException);

        verify(response).setContentType("application/json");
    }

    @DisplayName("Should clear WWW-Authenticate header when authentication fails")
    @Test
    public void shouldClearWwwAuthenticateHeaderWhenAuthenticationFails() throws IOException, ServletException {
        when(response.getWriter()).thenReturn(printWriter);

        AuthenticationEntryPoint authenticationEntryPoint = securityConfig.authenticationEntryPoint();
        authenticationEntryPoint.commence(request, response, authenticationException);

        verify(response).setHeader("WWW-Authenticate", "");
    }

    @DisplayName("Should write unauthorized error message to response body when authentication fails")
    @Test
    public void shouldWriteUnauthorizedErrorMessageToResponseBodyWhenAuthenticationFails() throws IOException, ServletException {
        when(response.getWriter()).thenReturn(printWriter);

        AuthenticationEntryPoint authenticationEntryPoint = securityConfig.authenticationEntryPoint();
        authenticationEntryPoint.commence(request, response, authenticationException);

        verify(response.getWriter()).write("{\"error\": \"Unauthorized access\"}");
    }

    @DisplayName("Should allow only frontend URL as origin")
    @Test
    public void shouldAllowOnlyFrontendUrlAsOrigin() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setServletPath("/");

        CorsConfigurationSource corsConfigurationSource = securityConfig.corsConfigurationSource();
        CorsConfiguration result = corsConfigurationSource.getCorsConfiguration(mockRequest);

        assertNotNull(result);
        assertNotNull(result.getAllowedOrigins());
        assertTrue(result.getAllowedOrigins().contains(FRONTEND_URL));
    }

    @DisplayName("Should allow GET, POST, PUT, DELETE and OPTIONS methods")
    @Test
    public void shouldAllowGetPostPutDeleteAndOptionsMethods() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setServletPath("/");

        CorsConfigurationSource corsConfigurationSource = securityConfig.corsConfigurationSource();
        CorsConfiguration result = corsConfigurationSource.getCorsConfiguration(mockRequest);

        assertNotNull(result);
        assertEquals(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"), result.getAllowedMethods());
    }

    @DisplayName("Should allow all headers")
    @Test
    public void shouldAllowAllHeaders() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setServletPath("/");

        CorsConfigurationSource corsConfigurationSource = securityConfig.corsConfigurationSource();
        CorsConfiguration result = corsConfigurationSource.getCorsConfiguration(mockRequest);

        assertNotNull(result);
        assertEquals(List.of("*"), result.getAllowedHeaders());
    }

    @DisplayName("Should allow credentials")
    @Test
    public void shouldAllowCredentials() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setServletPath("/");

        CorsConfigurationSource corsConfigurationSource = securityConfig.corsConfigurationSource();
        CorsConfiguration result = corsConfigurationSource.getCorsConfiguration(mockRequest);

        assertNotNull(result);
        assertEquals(true, result.getAllowCredentials());
    }

    @DisplayName("Should register CORS configuration for all paths")
    @Test
    public void shouldRegisterCorsConfigurationForAllPaths() {
        CorsConfigurationSource corsConfigurationSource = securityConfig.corsConfigurationSource();
        String[] paths = {"/", "/api/notes", "/api/auth/login", "/swagger-ui/index.html"};

        for(String path : paths) {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setServletPath(path);

            CorsConfiguration corsConfiguration = corsConfigurationSource.getCorsConfiguration(request);
            assertNotNull(corsConfiguration, "CORS configuration should be registered for path: " + path);
        }
    }
}
