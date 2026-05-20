package com.filahi.springboot.clipquest.controller;


import com.filahi.springboot.clipquest.response.CommentResponse;
import com.filahi.springboot.clipquest.response.UserResponse;
import com.filahi.springboot.clipquest.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommentService commentService;


    private final String BASE_URL = "/api/comments";


    private CommentResponse buildComment(long commentId) {
        return new CommentResponse(
                commentId,
                "mock-comment",
                LocalDateTime.now(),
                new UserResponse(
                        1L,
                        "mario",
                        "rossi",
                        29,
                        "mario.rossi@example.com",
                        "3978654320",
                        "/mock-path-url",
                        List.of()
                )
        );
    }


    @DisplayName("Should return all comments")
    @Test
    public void getAllCommentsTest() throws Exception {
        CommentResponse comment1 = buildComment(1L);
        CommentResponse comment2 = buildComment(2L);

        when(commentService.getAllComments(1L)).thenReturn(List.of(comment1, comment2));

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/video/{videoId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @DisplayName("Should return user comments")
    @Test
    @WithMockUser
    public void getUserCommentsTest() throws Exception {
        CommentResponse comment1 = buildComment(1L);
        CommentResponse comment2 = buildComment(2L);

        when(commentService.getUserComments(1L)).thenReturn(List.of(comment1, comment2));

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/user/{videoId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @DisplayName("Should add comment successfully")
    @Test
    @WithMockUser
    public void addCommentTest() throws Exception {
        String comment = "mock-comment";
        CommentResponse commentResponse = buildComment(1L);

        when(commentService.addComment(1L, comment)).thenReturn(commentResponse);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/user/{videoId}/new-comment", 1L)
                .param("comment", comment)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.comment").value(comment));
    }

    @DisplayName("Should delete comment successfully")
    @Test
    @WithMockUser
    public void deleteCommentTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/user/{commentId}", 1L))
                .andExpect(status().isOk());
    }
}
