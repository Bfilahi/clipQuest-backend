package com.filahi.springboot.clipquest.config;


import com.filahi.springboot.clipquest.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private ExpiredJwtException expiredJwtException;

    @Mock
    private RuntimeException exception;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @DisplayName("Should continue filter chain when authorization header is null")
    @Test
    public void shouldContinueFilterChainWhenAuthorizationHeaderIsNull() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(any(), any());
        verify(jwtService, never()).extractUsername(any());
        verifyNoInteractions(userDetailsService);
    }

    @DisplayName("Should continue filter chain when authorization header does not start with Bearer")
    @Test
    public void shouldContinueFilterChainWhenAuthorizationHeaderIsInvalid() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("invalid token");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(any(), any());
        verify(jwtService, never()).extractUsername(any());
        verifyNoInteractions(userDetailsService);
    }

    @DisplayName("Should authenticate user when token is valid")
    @Test
    public void shouldAuthenticateUserWhenTokenIsValid() throws ServletException, IOException {
        UserDetails userDetails = User.builder()
                .username("mario.rossi@example.com")
                .password("password")
                .roles("USER")
                .build();

        when(request.getHeader("Authorization")).thenReturn("Bearer mock-token");
        when(jwtService.extractUsername(any())).thenReturn("mario.rossi@example.com");
        when(userDetailsService.loadUserByUsername(any())).thenReturn(userDetails);
        when(jwtService.isTokenValid(any(), any())).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService).extractUsername("mock-token");
        verify(userDetailsService).loadUserByUsername("mario.rossi@example.com");
        verify(jwtService).isTokenValid("mock-token", userDetails);
        verify(filterChain).doFilter(request, response);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertTrue(authentication.isAuthenticated());
        assertEquals(userDetails, authentication.getPrincipal());
    }

    @DisplayName("Should not authenticate when extracted username is null")
    @Test
    public void shouldNotAuthenticateWhenUsernameIsNull() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer mock-token");
        when(jwtService.extractUsername(any())).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verifyNoInteractions(userDetailsService);
    }

    @DisplayName("Should not authenticate when token is invalid")
    @Test
    public void shouldNotAuthenticateWhenTokenIsInvalid() throws ServletException, IOException {
        UserDetails userDetails = User.builder()
                .username("mario.rossi@example.com")
                .password("password")
                .roles("USER")
                .build();

        when(request.getHeader("Authorization")).thenReturn("Bearer mock-token");
        when(jwtService.extractUsername(any())).thenReturn("mario.rossi@example.com");
        when(userDetailsService.loadUserByUsername(any())).thenReturn(userDetails);
        when(jwtService.isTokenValid(any(), any())).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }

    @DisplayName("Should set expired attribute when token is expired")
    @Test
    public void shouldSetExpiredAttributeWhenTokenIsExpired() throws ServletException, IOException {
        when(expiredJwtException.getMessage()).thenReturn("JWT token is expired");
        when(request.getHeader("Authorization")).thenReturn("Bearer mock-token");
        when(jwtService.extractUsername(any())).thenThrow(expiredJwtException);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(request).setAttribute("expired", "JWT token is expired");
        verify(filterChain).doFilter(request, response);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }

    @DisplayName("Should set invalid attribute when token processing fails")
    @Test
    public void shouldSetInvalidAttributeWhenTokenIsInvalid() throws ServletException, IOException {
        when(exception.getMessage()).thenReturn("JWT token is invalid");
        when(request.getHeader("Authorization")).thenReturn("Bearer mock-token");
        when(jwtService.extractUsername(any())).thenThrow(exception);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(request).setAttribute("invalid", "JWT token is invalid");
        verify(filterChain).doFilter(request, response);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }
}
