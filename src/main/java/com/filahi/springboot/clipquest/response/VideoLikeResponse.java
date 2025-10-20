package com.filahi.springboot.clipquest.response;

import com.filahi.springboot.clipquest.enumeration.LikeType;

public record VideoLikeResponse(
        LikeType userLikeStatus,
        int likesCount,
        int dislikesCount
) {
}
