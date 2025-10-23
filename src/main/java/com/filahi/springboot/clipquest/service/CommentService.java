package com.filahi.springboot.clipquest.service;

public interface CommentService {
    String addComment(String comment);
    String updateComment(long commentId, String comment);
    void deleteComment(long commentId);
}
