package com.filahi.springboot.clipquest.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.filahi.springboot.clipquest.entity.Authority;
import com.filahi.springboot.clipquest.entity.User;
import com.filahi.springboot.clipquest.entity.Video;
import com.filahi.springboot.clipquest.repository.VideoRepository;
import com.filahi.springboot.clipquest.request.VideoRequest;
import com.filahi.springboot.clipquest.response.UserResponse;
import com.filahi.springboot.clipquest.response.VideoResponse;
import com.filahi.springboot.clipquest.service.VideoService;
import com.filahi.springboot.clipquest.util.FindAuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@Service
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final FindAuthenticatedUser findAuthenticatedUser;
    private final Cloudinary cloudinary;

    public VideoServiceImpl(VideoRepository videoRepository, FindAuthenticatedUser findAuthenticatedUser, Cloudinary cloudinary) {
        this.videoRepository = videoRepository;
        this.findAuthenticatedUser = findAuthenticatedUser;
        this.cloudinary = cloudinary;
    }



    @Override
    public List<VideoResponse> getAllVideos() {
        List<Video> videos = this.videoRepository.findAll();
        return convertToVideoResponse(videos);
    }

    @Override
    public List<VideoResponse> getUserVideos() {
        User user = this.findAuthenticatedUser.getAuthenticatedUser();
        List<Video> videos = this.videoRepository.findByUser(user);
        return convertToVideoResponse(videos);
    }

    @Override
    public VideoResponse getVideo(long videoId) {
        Video video = this.videoRepository.findById(videoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return getVideoResponse(video);
    }

    @Override
    @Transactional
    public VideoResponse uploadVideo(VideoRequest videoRequest, MultipartFile file) {
        User user = this.findAuthenticatedUser.getAuthenticatedUser();

        Video video = new Video();
        video.setId(0);
        video.setTitle(videoRequest.title());
        video.setDescription(videoRequest.description());
        video.setUser(user);

        Map<?,?> uploadResult = uploadVideoToCloud(file);
        video.setFilePath(uploadResult.get("secure_url").toString());

        this.videoRepository.save(video);

        return getVideoResponse(video);
    }

    @Override
    public void deleteVideo(long videoId) {
        User user = this.findAuthenticatedUser.getAuthenticatedUser();

        Video video = this.videoRepository.findByUserAndId(user, videoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));

        this.videoRepository.delete(video);
    }

    @Override
    public VideoResponse editVideo(long videoId, VideoRequest videoRequest) {
        User user = this.findAuthenticatedUser.getAuthenticatedUser();

        Video video = this.videoRepository.findByUserAndId(user, videoId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));
        video.setTitle(videoRequest.title());
        video.setDescription(videoRequest.description());

        this.videoRepository.save(video);
        return getVideoResponse(video);
    }


    private Map<?,?> uploadVideoToCloud(MultipartFile file) {
        try{
            return this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "resource_type", "video",
                    "folder", "clipquest_videos"
            ));
        }
        catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private static List<VideoResponse> convertToVideoResponse(List<Video> videos) {
        return videos.stream().map(VideoServiceImpl::getVideoResponse).toList();
    }

    private static VideoResponse getVideoResponse(Video video) {
        return new VideoResponse(
                video.getId(),
                video.getTitle(),
                video.getDescription(),
                video.getFilePath(),
                new UserResponse(
                        video.getUser().getId(),
                        video.getUser().getFirstName(),
                        video.getUser().getLastName(),
                        video.getUser().getAge(),
                        video.getUser().getEmail(),
                        video.getUser().getPhoneNumber(),
                        video.getUser().getAuthorities().stream().map(auth -> (Authority) auth).toList()
                )
        );
    }
}
