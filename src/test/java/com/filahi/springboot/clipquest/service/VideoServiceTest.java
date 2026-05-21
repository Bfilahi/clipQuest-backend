package com.filahi.springboot.clipquest.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.Uploader;
import com.cloudinary.Url;
import com.filahi.springboot.clipquest.entity.Authority;
import com.filahi.springboot.clipquest.entity.User;
import com.filahi.springboot.clipquest.entity.Video;
import com.filahi.springboot.clipquest.repository.VideoRepository;
import com.filahi.springboot.clipquest.request.VideoRequest;
import com.filahi.springboot.clipquest.response.VideoResponse;
import com.filahi.springboot.clipquest.service.impl.VideoServiceImpl;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private FindAuthenticatedUser findAuthenticatedUser;

    @Mock
    private Uploader uploader;

    @Mock
    private Url url;

    @Mock
    private Cloudinary cloudinary;

    @InjectMocks
    private VideoServiceImpl videoService;



    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("mario");
        user.setLastName("rossi");
        user.setAge(29);
        user.setEmail("mario.rossi@example.com");
        user.setPhoneNumber("0123456789");
        user.setProfilePicture("/profile-picture");
        user.setAuthorities(List.of(new Authority("ROLE_USER")));
    }

    private Video buildVideo(long videoId) {
        Video video = new Video();
        video.setId(videoId);
        video.setTitle("Video Title");
        video.setUser(user);

        return video;
    }

    @DisplayName("Should return all videos")
    @Test
    public void getAllVideosTest() {
        Video video1 = buildVideo(1L);
        Video video2 = buildVideo(2L);
        Video video3 = buildVideo(3L);

        when(videoRepository.findAll()).thenReturn(List.of(video1, video2, video3));

        List<VideoResponse> result = videoService.getAllVideos();

        assertEquals(3, result.size());
    }

    @DisplayName("Should return user videos")
    @Test
    public void getUserVideosTest() {
        Video video1 = buildVideo(1L);
        Video video2 = buildVideo(2L);

        when(findAuthenticatedUser.getAuthenticatedUser()).thenReturn(user);
        when(videoRepository.findByUser(user)).thenReturn(List.of(video1, video2));

        List<VideoResponse> result = videoService.getUserVideos();

        assertEquals(2, result.size());
    }

    @DisplayName("Should return user videos")
    @Test
    public void getVideoTest() {
        Video video = buildVideo(1L);

        when(videoRepository.findById(video.getId())).thenReturn(Optional.of(video));

        VideoResponse result = videoService.getVideo(1L);

        assertEquals(1L, result.id());
    }

    @DisplayName("Should throw an exception when user is not found")
    @Test
    public void getVideoVideoNotFoundTest() {
        when(videoRepository.findById(1L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        assertThrows(ResponseStatusException.class, () -> videoService.getVideo(1L));
    }

    @DisplayName("Should upload video successfully")
    @Test
    public void uploadVideoTest() throws IOException {
        VideoRequest videoRequest = new VideoRequest("Video title update", "Video description update");
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "file.jpg",
                "image/jpg",
                "Hello world".getBytes()
        );

        when(findAuthenticatedUser.getAuthenticatedUser()).thenReturn(user);
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(), any())).thenReturn(Map.of(
                "public_id", "img-1",
                "secure_url", "http://test.com/image.jpg"
        ));
        when(cloudinary.url()).thenReturn(url);
        when(url.resourceType("video")).thenReturn(url);
        when(url.transformation(any(Transformation.class))).thenReturn(url);
        when(url.generate(any())).thenReturn("thumbnail-url");

        VideoResponse result = videoService.uploadVideo(videoRequest, file);

        ArgumentCaptor<Video> captor = ArgumentCaptor.forClass(Video.class);

        verify(videoRepository).save(captor.capture());
    }

    @DisplayName("Should delete video successfully")
    @Test
    public void deleteVideoTest() {
        Video video = buildVideo(1L);

        when(findAuthenticatedUser.getAuthenticatedUser()).thenReturn(user);
        when(videoRepository.findByUserAndId(user, video.getId())).thenReturn(Optional.of(video));
        when(cloudinary.uploader()).thenReturn(uploader);

        videoService.deleteVideo(video.getId());

        verify(videoRepository).delete(video);
    }

    @DisplayName("Should throw an exception when video is not found")
    @Test
    public void deleteVideoVideoNotFoundTest() {
        when(findAuthenticatedUser.getAuthenticatedUser()).thenReturn(user);
        when(videoRepository.findByUserAndId(user, 1L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));

        assertThrows(ResponseStatusException.class, () -> videoService.deleteVideo(1L));
    }

    @DisplayName("Should edit video successfully")
    @Test
    public void editVideoTest() {
        VideoRequest videoRequest = new VideoRequest("new video title", "new video description");
        Video video = buildVideo(1L);

        when(findAuthenticatedUser.getAuthenticatedUser()).thenReturn(user);
        when(videoRepository.findByUserAndId(user, video.getId())).thenReturn(Optional.of(video));

        VideoResponse result = videoService.editVideo(1L, videoRequest);

        verify(videoRepository).save(video);
        assertAll(
                () -> assertEquals(videoRequest.title(), result.title()),
                () -> assertEquals(videoRequest.description(), result.description())
        );
    }

    @DisplayName("Should throw an exception when video is not found")
    @Test
    public void editVideoTestVideoNotFoundTest() {
        VideoRequest videoRequest = new VideoRequest("new video title", "new video description");

        when(findAuthenticatedUser.getAuthenticatedUser()).thenReturn(user);
        when(videoRepository.findByUserAndId(user, 1L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));

        assertThrows(ResponseStatusException.class, () -> videoService.editVideo(1L, videoRequest));
    }
}
