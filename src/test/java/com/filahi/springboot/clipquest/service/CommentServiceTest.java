package com.filahi.springboot.clipquest.service;


import com.filahi.springboot.clipquest.entity.Authority;
import com.filahi.springboot.clipquest.entity.Comment;
import com.filahi.springboot.clipquest.entity.User;
import com.filahi.springboot.clipquest.entity.Video;
import com.filahi.springboot.clipquest.repository.CommentRepository;
import com.filahi.springboot.clipquest.repository.VideoRepository;
import com.filahi.springboot.clipquest.response.CommentResponse;
import com.filahi.springboot.clipquest.service.impl.CommentServiceImpl;
import com.filahi.springboot.clipquest.util.FindAuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private FindAuthenticatedUser findAuthenticatedUser;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Video video;
    private User user;


    @BeforeEach
    public void setUp () {
        video = new Video();
        video.setId(1L);

        user = new User();
        user.setId(1L);
        user.setAuthorities(List.of(new Authority("ROLE_USER")));
    }



    private Comment buildComment(long commentId) {
        return new Comment(commentId, "mock-comment", user, video);
    }


    @DisplayName("Should return all comments")
    @Test
    public void getAllCommentsTest() {
        Comment comment1 = buildComment(1L);
        Comment comment2 = buildComment(2L);
        Comment comment3 = buildComment(3L);

        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));
        when(commentRepository.findByVideo(video)).thenReturn(List.of(comment1, comment2, comment3));

        List<CommentResponse> result = commentService.getAllComments(1L);

        assertEquals(3, result.size());
    }

    @DisplayName("Should return user comments")
    @Test
    public void getUserCommentsTest() {
        Comment comment1 = buildComment(1L);
        Comment comment2 = buildComment(2L);

        when(findAuthenticatedUser.getAuthenticatedUser()).thenReturn(user);
        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));
        when(commentRepository.findByVideoAndUser(video, user)).thenReturn(List.of(comment1, comment2));

        List<CommentResponse> result = commentService.getUserComments(1L);

        assertEquals(2, result.size());
    }

    @DisplayName("Should throw an exception when video is not found")
    @Test
    public void getUserCommentsVideoNotFoundTest() {
        when(findAuthenticatedUser.getAuthenticatedUser()).thenReturn(user);
        when(videoRepository.findById(1L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));

        assertThrows(ResponseStatusException.class, () -> commentService.getUserComments(1L));
    }

    @DisplayName("Should add comment successfully")
    @Test
    public void addCommentTest() {
        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));
        when(findAuthenticatedUser.getAuthenticatedUser()).thenReturn(user);

        CommentResponse result = commentService.addComment(1L, "mock-comment");

        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);

        verify(commentRepository).save(captor.capture());

        assertEquals(0L, result.id());
        assertEquals("mock-comment", result.comment());
    }

    @DisplayName("Should throw an exception when video is not found")
    @Test
    public void addCommentVideoNotFoundTest() {
        when(videoRepository.findById(1L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));

        assertThrows(ResponseStatusException.class, () -> commentService.addComment(1L, "mock-comment"));
    }

    @DisplayName("Should delete comment successfully")
    @Test
    public void deleteCommentTest() {
        Comment comment = buildComment(1L);

        when(findAuthenticatedUser.getAuthenticatedUser()).thenReturn(user);
        when(commentRepository.findByUserAndId(user, 1L)).thenReturn(Optional.of(comment));

        commentService.deleteComment(1L);

        verify(commentRepository).delete(comment);
    }

    @DisplayName("Should throw an exception when comment is not found")
    @Test
    public void deleteCommentCommentNotFoundTest() {
        when(findAuthenticatedUser.getAuthenticatedUser()).thenReturn(user);
        when(commentRepository.findByUserAndId(user, 1L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        assertThrows(ResponseStatusException.class, () -> commentService.deleteComment(1L));
    }
}
