package com.filahi.springboot.clipquest.service;

import com.filahi.springboot.clipquest.enumeration.LikeType;
import com.filahi.springboot.clipquest.response.VideoLikeResponse;

public interface VideoInteractionService {
    VideoLikeResponse toggleLike(long videoId, LikeType likeType);
    void registerView(long videoId, String ipAddress);
    LikeType getUserLikeStatus(long videoId);
}
