package com.filahi.springboot.clipquest.controller;


import com.filahi.springboot.clipquest.request.VideoRequest;
import com.filahi.springboot.clipquest.response.VideoResponse;
import com.filahi.springboot.clipquest.service.VideoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/videos")
@Tag(name = "Video REST API Endpoints", description = "Operations related to videos")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }


    @PostMapping("/upload-video")
    public VideoResponse uploadVideo(@Valid @RequestBody VideoRequest videoRequest) {
        return this.videoService.uploadVideo(videoRequest);
    }
}
