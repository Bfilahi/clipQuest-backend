package com.filahi.springboot.clipquest.service;

import com.filahi.springboot.clipquest.request.VideoRequest;
import com.filahi.springboot.clipquest.response.VideoResponse;

public interface VideoService {
    VideoResponse uploadVideo(VideoRequest video);
}
