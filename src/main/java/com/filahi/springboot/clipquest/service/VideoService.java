package com.filahi.springboot.clipquest.service;

import com.filahi.springboot.clipquest.request.VideoRequest;
import com.filahi.springboot.clipquest.response.VideoResponse;

import java.util.List;

public interface VideoService {
    VideoResponse uploadVideo(VideoRequest video);
    List<VideoResponse> getVideos();
    void deleteVideo(long videoId);
    VideoResponse editVideo(long videoId, VideoRequest videoRequest);
}
