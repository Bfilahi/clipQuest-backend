package com.filahi.springboot.clipquest.response;

import com.filahi.springboot.clipquest.entity.Authority;

import java.util.List;

public record UserResponse(
        long id,
        String firstName,
        String lastName,
        String email,
        List<Authority> authorities
) {
}
