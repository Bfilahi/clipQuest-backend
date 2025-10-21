package com.filahi.springboot.clipquest.service.impl;

import com.filahi.springboot.clipquest.entity.User;
import com.filahi.springboot.clipquest.entity.Video;
import com.filahi.springboot.clipquest.repository.VideoRepository;
import com.filahi.springboot.clipquest.request.VideoRequest;
import com.filahi.springboot.clipquest.response.VideoResponse;
import com.filahi.springboot.clipquest.service.VideoService;
import com.filahi.springboot.clipquest.util.FindAuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final FindAuthenticatedUser findAuthenticatedUser;

    public VideoServiceImpl(VideoRepository videoRepository, FindAuthenticatedUser findAuthenticatedUser) {
        this.videoRepository = videoRepository;
        this.findAuthenticatedUser = findAuthenticatedUser;
    }



    @Override
    @Transactional
    public VideoResponse uploadVideo(VideoRequest videoRequest) {
        User user = this.findAuthenticatedUser.getAuthenticatedUser();

        Video video = new Video();
        video.setId(0);
        video.setTitle(videoRequest.title());
        video.setDescription(videoRequest.description());
        video.setFilePath(videoRequest.filePath());
        video.setUser(user);

        this.videoRepository.save(video);

        return new VideoResponse(
                video.getId(),
                video.getTitle(),
                video.getDescription(),
                video.getFilePath()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<VideoResponse> getVideos() {
        User user = this.findAuthenticatedUser.getAuthenticatedUser();
        List<Video> videos = this.videoRepository.findByUser(user);

        return videos.stream().map(video -> new VideoResponse(
                video.getId(),
                video.getTitle(),
                video.getDescription(),
                video.getFilePath()
        )).toList();
    }

    @Override
    public void deleteVideo(long videoId) {
        Video video = this.videoRepository.findById(videoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));

        this.videoRepository.delete(video);
    }

    @Override
    public VideoResponse editVideo(long videoId, VideoRequest videoRequest) {
        Video video = this.videoRepository.findById(videoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));

        video.setTitle(videoRequest.title());
        video.setDescription(videoRequest.description());

        this.videoRepository.save(video);

        return new VideoResponse(
                video.getId(),
                video.getTitle(),
                video.getDescription(),
                video.getFilePath()
        );
    }

}
