package com.filahi.springboot.clipquest.service;

import com.filahi.springboot.clipquest.request.VideoRequest;
import com.filahi.springboot.clipquest.response.VideoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {
    VideoResponse uploadVideo(VideoRequest video, MultipartFile file);
    List<VideoResponse> getVideos();
    VideoResponse getVideo(long videoId);
    void deleteVideo(long videoId);
    VideoResponse editVideo(long videoId, VideoRequest videoRequest);
}
