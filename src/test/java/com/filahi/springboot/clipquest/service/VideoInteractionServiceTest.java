package com.filahi.springboot.clipquest.service;


import com.filahi.springboot.clipquest.entity.*;
import com.filahi.springboot.clipquest.enumeration.LikeType;
import com.filahi.springboot.clipquest.repository.VideoLikeRepository;
import com.filahi.springboot.clipquest.repository.VideoRepository;
import com.filahi.springboot.clipquest.repository.VideoViewRepository;
import com.filahi.springboot.clipquest.response.VideoLikeResponse;
import com.filahi.springboot.clipquest.service.impl.VideoInteractionServiceImpl;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VideoInteractionServiceTest {

    @Mock
    private FindAuthenticatedUser findAuthenticatedUser;

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private VideoLikeRepository videoLikeRepository;

    @Mock
    private VideoViewRepository videoViewRepository;

    @InjectMocks
    private VideoInteractionServiceImpl videoInteractionService;

    private User user;
    private VideoLike videoLike;

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


        videoLike = new VideoLike();
        videoLike.setType(LikeType.LIKE);
    }

    private Video buildVideo(long videoId) {
        Video video = new Video();
        video.setId(videoId);
        video.setTitle("Video Title");
        video.setUser(user);
        video.setCachedViewsCount(1);
        video.setCachedLikesCount(100);
        video.setCachedDislikesCount(5);

        return video;
    }



    @DisplayName("Should throw an exception when video is not found")
    @Test
    public void toggleLikeVideoNotFoundTest() {
        when(findAuthenticatedUser.getAuthenticatedUser()).thenReturn(user);
        when(videoRepository.findById(1L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));

        assertThrows(ResponseStatusException.class, () -> videoInteractionService.toggleLike(1L, LikeType.LIKE));
    }

    @DisplayName("Should handle non-existing video status")
    @Test
    public void toggleLikeNoStatusTest() {
        Video video = buildVideo(1L);

        when(findAuthenticatedUser.getAuthenticatedUser()).thenReturn(user);
        when(videoRepository.findById(video.getId())).thenReturn(Optional.of(video));
        when(videoLikeRepository.findByUserAndVideo(user, video)).thenReturn(Optional.empty());
        when(videoLikeRepository.countByVideoAndType(video, LikeType.LIKE)).thenReturn((long) video.getCachedLikesCount());
        when(videoLikeRepository.countByVideoAndType(video, LikeType.DISLIKE)).thenReturn((long) video.getCachedDislikesCount());

        VideoLikeResponse result = videoInteractionService.toggleLike(video.getId(), LikeType.LIKE);

        ArgumentCaptor<VideoLike> captor = ArgumentCaptor.forClass(VideoLike.class);

        verify(videoLikeRepository).save(captor.capture());

        assertEquals(LikeType.LIKE, result.userLikeStatus());
        assertEquals(video.getCachedLikesCount(), result.likesCount());
        assertEquals(video.getCachedDislikesCount(), result.dislikesCount());
    }

    @DisplayName("Should handle video same like status")
    @Test
    public void toggleLikeSameStatusTest() {
        Video video = buildVideo(1L);

        when(findAuthenticatedUser.getAuthenticatedUser()).thenReturn(user);
        when(videoRepository.findById(video.getId())).thenReturn(Optional.of(video));
        when(videoLikeRepository.findByUserAndVideo(user, video)).thenReturn(Optional.of(videoLike));
        when(videoLikeRepository.countByVideoAndType(video, LikeType.LIKE)).thenReturn((long) video.getCachedLikesCount());
        when(videoLikeRepository.countByVideoAndType(video, LikeType.DISLIKE)).thenReturn((long) video.getCachedDislikesCount());

        VideoLikeResponse result = videoInteractionService.toggleLike(video.getId(), LikeType.LIKE);

        ArgumentCaptor<VideoLike> captor = ArgumentCaptor.forClass(VideoLike.class);

        verify(videoLikeRepository).delete(captor.capture());

        assertNull(result.userLikeStatus());
        assertEquals(video.getCachedLikesCount(), result.likesCount());
        assertEquals(video.getCachedDislikesCount(), result.dislikesCount());
    }

    @DisplayName("Should handle video different like status")
    @Test
    public void toggleLikeDifferentStatusTest() {
        Video video = buildVideo(1L);

        when(findAuthenticatedUser.getAuthenticatedUser()).thenReturn(user);
        when(videoRepository.findById(video.getId())).thenReturn(Optional.of(video));
        when(videoLikeRepository.findByUserAndVideo(user, video)).thenReturn(Optional.of(videoLike));
        when(videoLikeRepository.countByVideoAndType(video, LikeType.LIKE)).thenReturn((long) video.getCachedLikesCount());
        when(videoLikeRepository.countByVideoAndType(video, LikeType.DISLIKE)).thenReturn((long) video.getCachedDislikesCount());

        VideoLikeResponse result = videoInteractionService.toggleLike(video.getId(), LikeType.DISLIKE);

        ArgumentCaptor<VideoLike> captor = ArgumentCaptor.forClass(VideoLike.class);

        verify(videoLikeRepository).save(captor.capture());

        assertEquals(LikeType.DISLIKE, result.userLikeStatus());
        assertEquals(video.getCachedLikesCount(), result.likesCount());
        assertEquals(video.getCachedDislikesCount(), result.dislikesCount());
    }

    @DisplayName("Should throw an exception when video is not found")
    @Test
    public void registerViewVideoNotFoundTest() {
        when(videoRepository.findById(1L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));

        assertThrows(ResponseStatusException.class, () -> videoInteractionService.registerView(1L, "mock-ip-address"));
    }

    @DisplayName("Should handle view exists within 24 hours")
    @Test
    public void registerViewViewAlreadyExistsTest() {
        Video video = buildVideo(1L);

        when(videoRepository.findById(video.getId())).thenReturn(Optional.of(video));
        when(videoViewRepository.existsByIpAddressAndVideoAndViewedAtAfter(any(), any(), any())).thenReturn(true);

        videoInteractionService.registerView(video.getId(), "mock-ip-address");

        verify(videoViewRepository, never()).save(any());
        verify(videoRepository, never()).save(any());
    }

    @DisplayName("Should register view successfully")
    @Test
    public void registerViewTest() {
        Video video = buildVideo(1L);

        when(videoRepository.findById(video.getId())).thenReturn(Optional.of(video));
        when(videoViewRepository.existsByIpAddressAndVideoAndViewedAtAfter(any(), any(), any())).thenReturn(false);

        videoInteractionService.registerView(video.getId(), "mock-ip-address");

        ArgumentCaptor<VideoView> captor = ArgumentCaptor.forClass(VideoView.class);
        verify(videoViewRepository).save(captor.capture());
        verify(videoRepository).save(video);

        assertEquals(2, video.getCachedViewsCount());
    }

    @DisplayName("Should throw an exception when video is not found")
    @Test
    public void getUserLikeStatusVideoNotFoundTest() {
        when(findAuthenticatedUser.getAuthenticatedUser()).thenReturn(user);
        when(videoRepository.findById(1L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));

        assertThrows(ResponseStatusException.class, () -> videoInteractionService.getUserLikeStatus(1L));
    }

    @DisplayName("Should return user like status")
    @Test
    public void getUserLikeStatusTest() {
        Video video = buildVideo(1L);

        VideoLike videoLike = new VideoLike();
        videoLike.setType(LikeType.LIKE);

        when(findAuthenticatedUser.getAuthenticatedUser()).thenReturn(user);
        when(videoRepository.findById(video.getId())).thenReturn(Optional.of(video));
        when(videoLikeRepository.findByUserAndVideo(user, video)).thenReturn(Optional.of(videoLike));

        LikeType result = videoInteractionService.getUserLikeStatus(video.getId());

        assertEquals(videoLike.getType(), result);
    }

}
