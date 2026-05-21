package com.filahi.springboot.clipquest.service;


import com.filahi.springboot.clipquest.entity.User;
import com.filahi.springboot.clipquest.repository.UserRepository;
import com.filahi.springboot.clipquest.request.LoginRequest;
import com.filahi.springboot.clipquest.request.RegisterRequest;
import com.filahi.springboot.clipquest.response.LoginResponse;
import com.filahi.springboot.clipquest.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private User user;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    public void setUp() {
        registerRequest = new RegisterRequest(
                "mario",
                "rossi",
                29,
                "mario.rossi@example.com",
                "Password123!",
                "0123456789"
        );

        loginRequest = new LoginRequest(
                "mario.rossi@example.com",
                "Password123!"
        );

        user = new User();
        user.setId(1L);
        user.setEmail("mario.rossi@example.com");
    }

    @DisplayName("Should signup successfully")
    @Test
    public void signupTest() throws Exception {
        when(userRepository.findByEmail(registerRequest.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.password())).thenReturn(registerRequest.password());

        authenticationService.signup(registerRequest);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(captor.capture());
    }

    @DisplayName("Should throw an exception when email is taken")
    @Test
    public void registerEmailTakenTest() {
        when(userRepository.findByEmail(registerRequest.email())).thenReturn(Optional.of(new User()));

        assertThrows(Exception.class, () -> authenticationService.signup(registerRequest));
    }

    @DisplayName("Should login successfully")
    @Test
    public void loginTest() {
        when(userRepository.findByEmail(loginRequest.email())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(), any())).thenReturn("mock-token");

        LoginResponse result = authenticationService.login(loginRequest);

        assertEquals("mock-token", result.token());
    }

    @DisplayName("Should throw an exception when user is not found")
    @Test
    public void loginUserNotFoundTest() {
        when(userRepository.findByEmail(any())).thenThrow(new IllegalArgumentException("Invalid credentials"));

        assertThrows(IllegalArgumentException.class, () -> authenticationService.login(loginRequest));
    }
}
