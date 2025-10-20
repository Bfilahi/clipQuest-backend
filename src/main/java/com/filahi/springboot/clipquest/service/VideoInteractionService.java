package com.filahi.springboot.clipquest.service;

import com.filahi.springboot.clipquest.entity.User;
import com.filahi.springboot.clipquest.enumeration.LikeType;
import com.filahi.springboot.clipquest.response.VideoLikeResponse;

public interface VideoInteractionService {
    VideoLikeResponse toggleLike(long videoId, User user, LikeType likeType);
    void registerView(long videoId, User user, String ipAddress);
    LikeType getUserLikeStatus(long videoId, User user);
}
