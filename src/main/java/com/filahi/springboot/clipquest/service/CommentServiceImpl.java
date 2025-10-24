package com.filahi.springboot.clipquest.service;


import com.filahi.springboot.clipquest.entity.Authority;
import com.filahi.springboot.clipquest.entity.Comment;
import com.filahi.springboot.clipquest.entity.User;
import com.filahi.springboot.clipquest.entity.Video;
import com.filahi.springboot.clipquest.repository.CommentRepository;
import com.filahi.springboot.clipquest.repository.VideoRepository;
import com.filahi.springboot.clipquest.response.CommentResponse;
import com.filahi.springboot.clipquest.response.UserResponse;
import com.filahi.springboot.clipquest.util.FindAuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final VideoRepository videoRepository;
    private final FindAuthenticatedUser findAuthenticatedUser;

    public CommentServiceImpl(CommentRepository commentRepository, VideoRepository videoRepository, FindAuthenticatedUser findAuthenticatedUser) {
        this.commentRepository = commentRepository;
        this.videoRepository = videoRepository;
        this.findAuthenticatedUser = findAuthenticatedUser;
    }


    @Override
    public List<CommentResponse> getAllComments(long videoId) {
        Video video = this.videoRepository.findById(videoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));

        List<Comment> comments = this.commentRepository.findByVideo(video);
        return convertToCommentResponse(comments);
    }

    @Override
    public List<CommentResponse> getUserComments() {
        User user = this.findAuthenticatedUser.getAuthenticatedUser();
        List<Comment> comments = this.commentRepository.findByUser(user);
        return convertToCommentResponse(comments);
    }

    @Override
    @Transactional
    public CommentResponse addComment(long videoId, String comment) {
        Video video = this.videoRepository.findById(videoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));

        User user = this.findAuthenticatedUser.getAuthenticatedUser();

        Comment newComment = new Comment();
        newComment.setId(0);
        newComment.setComment(comment);
        newComment.setVideo(video);
        newComment.setUser(user);

        this.commentRepository.save(newComment);
        return convertToCommentResponse(newComment);
    }


    @Override
    @Transactional
    public CommentResponse updateComment(long commentId, String comment) {
        User user = this.findAuthenticatedUser.getAuthenticatedUser();
        Comment theComment = this.commentRepository.findByUserAndId(user, commentId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
        theComment.setComment(comment);
        this.commentRepository.save(theComment);
        return convertToCommentResponse(theComment);
    }

    @Override
    @Transactional
    public void deleteComment(long commentId) {
        User user = this.findAuthenticatedUser.getAuthenticatedUser();
        Comment comment = this.commentRepository.findByUserAndId(user, commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
        this.commentRepository.delete(comment);
    }



    private static List<CommentResponse> convertToCommentResponse(List<Comment> comments) {
        return comments.stream().map(comment ->
                new CommentResponse(
                        comment.getComment(),
                        comment.getCreatedDate(),
                        new UserResponse(
                                comment.getUser().getId(),
                                comment.getUser().getFirstName(),
                                comment.getUser().getLastName(),
                                comment.getUser().getAge(),
                                comment.getUser().getEmail(),
                                comment.getUser().getPhoneNumber(),
                                comment.getUser().getAuthorities().stream().map(auth -> (Authority) auth).toList()
                        )
                )).toList();
    }

    private static CommentResponse convertToCommentResponse(Comment newComment) {
        return new CommentResponse(
                newComment.getComment(),
                newComment.getCreatedDate(),
                new UserResponse(
                        newComment.getUser().getId(),
                        newComment.getUser().getFirstName(),
                        newComment.getUser().getLastName(),
                        newComment.getUser().getAge(),
                        newComment.getUser().getEmail(),
                        newComment.getUser().getPhoneNumber(),
                        newComment.getUser().getAuthorities().stream().map(auth -> (Authority) auth).toList()
                )
        );
    }
}
