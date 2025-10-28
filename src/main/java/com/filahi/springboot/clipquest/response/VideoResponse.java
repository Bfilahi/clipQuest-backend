package com.filahi.springboot.clipquest.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record VideoResponse(
    long id,
    String title,
    String description,
    String pathFile,
    String cloudinaryPublicId,
    String thumbnailUrl,
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDateTime createdDate,
    UserResponse user,
    VideoLikeResponse videoLikeResponse,
    int views
) {
}
