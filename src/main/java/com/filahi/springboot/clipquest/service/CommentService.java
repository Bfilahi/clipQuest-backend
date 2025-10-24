package com.filahi.springboot.clipquest.service;

import com.filahi.springboot.clipquest.response.CommentResponse;

import java.util.List;

public interface CommentService {
    List<CommentResponse> getAllComments(long videoId);
    List<CommentResponse> getUserComments();
    CommentResponse addComment(long videoId, String comment);
    CommentResponse updateComment(long commentId, String comment);
    void deleteComment(long commentId);
}
