package com.filahi.springboot.clipquest.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.filahi.springboot.clipquest.enumeration.LikeType;
import com.filahi.springboot.clipquest.request.VideoRequest;
import com.filahi.springboot.clipquest.response.UserResponse;
import com.filahi.springboot.clipquest.response.VideoLikeResponse;
import com.filahi.springboot.clipquest.response.VideoResponse;
import com.filahi.springboot.clipquest.service.VideoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class VideoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VideoService videoService;


    private final String BASE_URL = "/api/videos";


    private VideoResponse buildVideo(long videoId){
        return new VideoResponse(
                videoId,
                "Video-" + videoId + " title",
                "Video-" + videoId + " description",
                "/Video-" + videoId + "-path-file",
                "mock-cloudinary-id",
                "/mock-thumbnail-url",
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
                ),
                new VideoLikeResponse(
                        LikeType.LIKE,
                        100,
                        10
                ),
                2000
        );
    }

    @DisplayName("Should return all videos")
    @Test
    public void getAllVideosTest() throws Exception {
        VideoResponse videoResponse1 = buildVideo(1L);
        VideoResponse videoResponse2 = buildVideo(2L);
        VideoResponse videoResponse3 = buildVideo(3L);

        when(videoService.getAllVideos()).thenReturn(List.of(videoResponse1, videoResponse2, videoResponse3));

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @DisplayName("Should return user videos")
    @Test
    @WithMockUser
    public void getUserVideosTest() throws Exception {
        VideoResponse videoResponse1 = buildVideo(1L);
        VideoResponse videoResponse2 = buildVideo(2L);

        when(videoService.getUserVideos()).thenReturn(List.of(videoResponse1, videoResponse2));

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @DisplayName("Should return a specific video")
    @Test
    public void getVideoTest() throws Exception {
        VideoResponse videoResponse = buildVideo(1L);

        when(videoService.getVideo(1L)).thenReturn(videoResponse);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/video/{videoId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @DisplayName("Should throw an exception when video is not found")
    @Test
    public void getVideoVideoNotFoundTest() throws Exception {
        when(videoService.getVideo(1L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/video/{videoId}", 1L))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Should upload video successfully")
    @Test
    @WithMockUser
    public void uploadVideoTest() throws Exception {
        VideoResponse videoResponse = buildVideo(1L);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-image.jpg",
                "image/jpeg",
                "Hello world".getBytes()
        );

        when(videoService.uploadVideo(any(VideoRequest.class), any(MultipartFile.class))).thenReturn(videoResponse);

        mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL + "/user/upload-video")
                        .file(file)
                .param("title", videoResponse.title())
                .param("description", videoResponse.description())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value(videoResponse.title()));
    }

    @DisplayName("Should delete video successfully")
    @Test
    @WithMockUser
    public void deleteVideoTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/user/{videoId}", 1L))
                .andExpect(status().isOk());
    }

    @DisplayName("Should edit video successfully")
    @Test
    @WithMockUser
    public void editVideoTest() throws Exception {
        VideoResponse videoResponse = buildVideo(1L);
        VideoRequest videoRequest = new VideoRequest("video title", "video description");

        when(videoService.editVideo(1L, videoRequest)).thenReturn(videoResponse);

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/user/edit/{videoId}/video", 1L)
                .content(new ObjectMapper().writeValueAsString(videoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());
    }

    @DisplayName("Should throw an exception when video is not found")
    @Test
    @WithMockUser
    public void editVideoVideoNotFoundTest() throws Exception {
        VideoRequest videoRequest = new VideoRequest("video title", "video description");

        when(videoService.editVideo(1L, videoRequest)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/user/edit/{videoId}/video", 1L)
                        .content(new ObjectMapper().writeValueAsString(videoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }
}
