package com.filahi.springboot.clipquest.controller;


import com.filahi.springboot.clipquest.request.VideoRequest;
import com.filahi.springboot.clipquest.response.VideoResponse;
import com.filahi.springboot.clipquest.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/user/videos")
@Tag(name = "Video REST API Endpoints", description = "Operations related to videos")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }


    @Operation(summary = "Get all videos", description = "Retrieve list of current user's videos")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<VideoResponse> getVideos() {
        return this.videoService.getVideos();
    }

    @Operation(summary = "Get a video", description = "Retrieve a video by id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{videoId}")
    public VideoResponse getVideo(@PathVariable long videoId) {
        return this.videoService.getVideo(videoId);
    }

    @Operation(summary = "Upload video", description = "Upload video and save it to database")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/upload-video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public VideoResponse uploadVideo(@RequestParam String title,
                                     @RequestParam String description,
                                     @RequestPart MultipartFile file) {

        VideoRequest videoRequest = new VideoRequest(title, description);
        return this.videoService.uploadVideo(videoRequest, file);
    }

    @Operation(summary = "Delete video", description = "Delete video from database")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{videoId}")
    public void deleteVideo(@PathVariable long videoId) {
        this.videoService.deleteVideo(videoId);
    }

    @Operation(summary = "Update video", description = "Update video")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("edit/{videoId}/video")
    public VideoResponse editVideo(@PathVariable long videoId,
                                   @Valid @RequestBody VideoRequest videoRequest) {

        return this.videoService.editVideo(videoId, videoRequest);
    }
}
