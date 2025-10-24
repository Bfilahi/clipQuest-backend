package com.filahi.springboot.clipquest.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CommentResponse(
        String comment,

        @JsonFormat(pattern = "yyy-MM-dd")
        LocalDateTime createdDate,

        UserResponse user
) {
}
