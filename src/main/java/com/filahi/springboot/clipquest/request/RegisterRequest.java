package com.filahi.springboot.clipquest.request;

import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotEmpty(message = "Firstname is mandatory")
        @Size(min = 3, max = 30, message = "Firstname must be at least 3 characters long")
        String firstName,

        @NotEmpty(message = "Lastname is mandatory")
        @Size(min = 3, max = 30, message = "Lastname must be at least 3 characters long")
        String lastName,

        @Min(value = 18, message = "Age must be at least 18")
        @Max(value = 100, message = "Age must be at most 100")
        int age,

        @NotEmpty(message = "Email is mandatory")
        @Email(message = "Invalid email format")
        String email,

        @NotEmpty(message = "Password is mandatory")
        String password,

        @NotEmpty(message = "Phone number is mandatory")
        String phoneNumber
) {
}
