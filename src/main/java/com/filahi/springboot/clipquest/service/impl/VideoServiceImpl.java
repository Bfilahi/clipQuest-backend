package com.filahi.springboot.clipquest.service.impl;

import com.filahi.springboot.clipquest.entity.Video;
import com.filahi.springboot.clipquest.repository.VideoRepository;
import com.filahi.springboot.clipquest.request.VideoRequest;
import com.filahi.springboot.clipquest.response.VideoResponse;
import com.filahi.springboot.clipquest.service.VideoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;

    public VideoServiceImpl(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }



    @Override
    @Transactional
    public VideoResponse uploadVideo(VideoRequest videoRequest) {
        Video videoEntity = new Video();
        videoEntity.setId(0);
        videoEntity.setTitle(videoRequest.title());
        videoEntity.setDescription(videoRequest.description());
        videoEntity.setFilePath(videoRequest.filePath());

        this.videoRepository.save(videoEntity);

        return new VideoResponse(
                videoEntity.getId(),
                videoEntity.getTitle(),
                videoEntity.getDescription(),
                videoEntity.getFilePath()
        );
    }
}
