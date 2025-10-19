package com.filahi.springboot.clipquest.service;

import com.filahi.springboot.clipquest.request.RegisterRequest;

public interface AuthenticationService {
    void signup(RegisterRequest registerRequest) throws Exception;
}
