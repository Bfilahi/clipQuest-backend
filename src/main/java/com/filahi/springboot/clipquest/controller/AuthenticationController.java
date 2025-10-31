package com.filahi.springboot.clipquest.controller;


import com.filahi.springboot.clipquest.request.LoginRequest;
import com.filahi.springboot.clipquest.request.RegisterRequest;
import com.filahi.springboot.clipquest.response.LoginResponse;
import com.filahi.springboot.clipquest.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication REST API Endpoints", description = "Operations related to user authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @Operation(summary = "User signup", description = "Create new user in database")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public void signup(@Valid @RequestBody RegisterRequest  registerRequest) throws Exception {
        this.authenticationService.signup(registerRequest);
    }

    @Operation(summary = "User login", description = "Authenticating the user")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return this.authenticationService.login(loginRequest);
    }
}
