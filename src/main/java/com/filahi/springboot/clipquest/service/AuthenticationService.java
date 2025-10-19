package com.filahi.springboot.clipquest.service;

import com.filahi.springboot.clipquest.request.LoginRequest;
import com.filahi.springboot.clipquest.request.RegisterRequest;
import com.filahi.springboot.clipquest.response.LoginResponse;

public interface AuthenticationService {
    void signup(RegisterRequest registerRequest) throws Exception;
    LoginResponse login(LoginRequest loginRequest);
}
