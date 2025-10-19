package com.filahi.springboot.clipquest.service.impl;

import com.filahi.springboot.clipquest.entity.Authority;
import com.filahi.springboot.clipquest.entity.User;
import com.filahi.springboot.clipquest.repository.UserRepository;
import com.filahi.springboot.clipquest.request.RegisterRequest;
import com.filahi.springboot.clipquest.service.AuthenticationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }



    @Override
    @Transactional
    public void signup(RegisterRequest registerRequest) throws Exception {
        if(isEmailTaken(registerRequest.email()))
            throw new Exception("Email already taken");

        User user = buildNewUser(registerRequest);
        this.userRepository.save(user);
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
