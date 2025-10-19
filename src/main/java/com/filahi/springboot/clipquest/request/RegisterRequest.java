package com.filahi.springboot.clipquest.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotEmpty(message = "Firstname is mandatory")
        @Size(min = 3, max = 30, message = "Firstname must be at least 3 characters long")
        String firstName,

        @NotEmpty(message = "Lastname is mandatory")
        @Size(min = 3, max = 30, message = "Lastname must be at least 3 characters long")
        String lastName,

        @NotEmpty(message = "Username is mandatory")
        String username,

        @NotEmpty(message = "Email is mandatory")
        @Email(message = "Invalid email format")
        String email,

        @NotEmpty(message = "Password is mandatory")
        String password
) {
}
