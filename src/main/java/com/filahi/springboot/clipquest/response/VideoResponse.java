package com.filahi.springboot.clipquest.response;

public record VideoResponse(
    long id,
    String title,
    String description,
    String pathFile,
    UserResponse user
) {
}
