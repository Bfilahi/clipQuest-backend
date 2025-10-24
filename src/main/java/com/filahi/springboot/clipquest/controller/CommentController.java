package com.filahi.springboot.clipquest.controller;


import com.filahi.springboot.clipquest.response.CommentResponse;
import com.filahi.springboot.clipquest.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@Tag(name = "Comments REST API Endpoints", description = "Operations related to user's comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }



    @Operation(summary = "Get all comments", description = "Retrieve list of a video's comments")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/video/{videoId}")
    public List<CommentResponse> getAllComments(@PathVariable long videoId) {
        return this.commentService.getAllComments(videoId);
    }

    @Operation(summary = "Get user comments", description = "Retrieve list of all comments made by a user")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user")
    public List<CommentResponse> getUserComments() {
        return this.commentService.getUserComments();
    }

    @Operation(summary = "Add a comment", description = "Save a comment to database")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/user/{videoId}/new-comment")
    public CommentResponse addComment(@PathVariable long videoId, String comment){
        return this.commentService.addComment(videoId, comment);
    }

    @Operation(summary = "Update a comment", description = "Update a comment")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/user/{commentId}")
    public CommentResponse updateComment(@PathVariable long commentId, String comment){
        return this.commentService.updateComment(commentId, comment);
    }

    @Operation(summary = "Delete a comment", description = "Delete a comment from database")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/user/{commentId}")
    public void deleteComment(@PathVariable long commentId){
        this.commentService.deleteComment(commentId);
    }
}
