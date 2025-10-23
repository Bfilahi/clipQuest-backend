package com.filahi.springboot.clipquest.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@Tag(name = "Comments REST API Endpoints", description = "Operations related to user's comments")
public class CommentController {


    @Operation(summary = "Add a comment", description = "Save a comment to database")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public String addComment(String comment){
        return "";
    }

    @Operation(summary = "Update a comment", description = "Update a comment")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{commentId}")
    public String updateComment(@PathVariable long commentId, String comment){
        return "";
    }

    @Operation(summary = "Delete a comment", description = "Delete a comment from database")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable long commentId){

    }
}
