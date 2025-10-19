package com.filahi.springboot.clipquest.service.impl;

import com.filahi.springboot.clipquest.entity.Authority;
import com.filahi.springboot.clipquest.entity.User;
import com.filahi.springboot.clipquest.repository.UserRepository;
import com.filahi.springboot.clipquest.request.LoginRequest;
import com.filahi.springboot.clipquest.request.RegisterRequest;
import com.filahi.springboot.clipquest.response.LoginResponse;
import com.filahi.springboot.clipquest.service.AuthenticationService;
import com.filahi.springboot.clipquest.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }



    @Override
    @Transactional
    public void signup(RegisterRequest registerRequest) throws Exception {
        if(isEmailTaken(registerRequest.email()))
            throw new Exception("Email already taken");

        User user = buildNewUser(registerRequest);
        this.userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest loginRequest) {
        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );

        User user = this.userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        String token = this.jwtService.generateToken(new HashMap<>(), user);

        return new LoginResponse(token);
    }


    private boolean isEmailTaken(String email) {
        return this.userRepository.findByEmail(email).isPresent();
    }

    private User buildNewUser(RegisterRequest registerRequest) {
        User user = new User();
        user.setId(0);
        user.setFirstName(registerRequest.firstName());
        user.setLastName(registerRequest.lastName());
        user.setUsername(registerRequest.username());
        user.setEmail(registerRequest.email());
        user.setPassword(this.passwordEncoder.encode(registerRequest.password()));
        user.setAuthorities(getAuthorities());

        return user;
    }

    private List<Authority> getAuthorities() {
        List<Authority> authorities = new ArrayList<>();
        authorities.add(new Authority("ROLE_USER"));

        return authorities;
    }
}
