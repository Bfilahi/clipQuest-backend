package com.filahi.springboot.clipquest.controller;


import com.filahi.springboot.clipquest.enumeration.LikeType;
import com.filahi.springboot.clipquest.response.VideoLikeResponse;
import com.filahi.springboot.clipquest.service.VideoInteractionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/videos")
@Tag(name = "Video interaction REST API Endpoints", description = "Operations related to video interactions")
public class VideoInteractionController {

    private final VideoInteractionService videoInteractionService;

    public VideoInteractionController(VideoInteractionService videoInteractionService) {
        this.videoInteractionService = videoInteractionService;
    }


    @Operation(summary = "Like a video", description = "Saving likes to database")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{videoId}/like")
    public VideoLikeResponse likeVideo(@PathVariable long videoId){

        return this.videoInteractionService.toggleLike(videoId, LikeType.LIKE);
    }

    @Operation(summary = "Dislike video", description = "Saving dislikes to database")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{videoId}/dislike")
    public VideoLikeResponse dislikeVideo(@PathVariable long videoId) {

        return this.videoInteractionService.toggleLike(videoId, LikeType.DISLIKE);
    }


    @Operation(summary = "View a video", description = "Saving view to database")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{videoId}/view")
    public void registerView(@PathVariable long videoId,
                             HttpServletRequest request) {

        String ipAddress = request.getRemoteAddr();
        this.videoInteractionService.registerView(videoId, ipAddress);
    }


    @GetMapping("/{videoId}/like-status")
    public LikeType getLikeStatus(@PathVariable long videoId) {

        return this.videoInteractionService.getUserLikeStatus(videoId);
    }
}
