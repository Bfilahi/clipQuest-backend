package com.filahi.springboot.clipquest.controller;


import com.filahi.springboot.clipquest.enumeration.LikeType;
import com.filahi.springboot.clipquest.response.VideoLikeResponse;
import com.filahi.springboot.clipquest.service.VideoInteractionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class VideoInteractionControllerTest {
    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private VideoInteractionService videoInteractionService;


    private final String BASE_URL = "/api/videos";


    @DisplayName("Should set the video status to like")
    @Test
    @WithMockUser
    public void likeVideoTest() throws Exception {
        VideoLikeResponse response = new VideoLikeResponse(
                LikeType.LIKE,
                1000,
                20
        );

        when(videoInteractionService.toggleLike(1L, LikeType.LIKE)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/{videoId}/like", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userLikeStatus").value(LikeType.LIKE.toString()));
        ;
    }

    @DisplayName("Should throw an exception when no video is found")
    @Test
    @WithMockUser
    public void likeVideoVideoNotFoundTest() throws Exception {
        when(videoInteractionService.toggleLike(1L, LikeType.LIKE))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/{videoId}/like", 1L))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Should set the video status to dislike")
    @Test
    @WithMockUser
    public void dislikeVideoTest() throws Exception {
        VideoLikeResponse response = new VideoLikeResponse(
                LikeType.DISLIKE,
                1000,
                20
        );

        when(videoInteractionService.toggleLike(1L, LikeType.DISLIKE)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/{videoId}/dislike", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userLikeStatus").value(LikeType.DISLIKE.toString()));
    }

    @DisplayName("Should register a video visualization correctly")
    @Test
    public void registerViewTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/{videoId}/view", 1L))
                .andExpect(status().isOk());
    }

    @DisplayName("Should video like status")
    @Test
    @WithMockUser
    public void getLikeStatusTest() throws Exception {
        when(videoInteractionService.getUserLikeStatus(1L)).thenReturn(LikeType.LIKE);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{videoId}/like-status", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(LikeType.LIKE.toString()));
    }
}
