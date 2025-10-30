package com.filahi.springboot.clipquest.service.impl;

import com.filahi.springboot.clipquest.entity.User;
import com.filahi.springboot.clipquest.entity.Video;
import com.filahi.springboot.clipquest.entity.VideoLike;
import com.filahi.springboot.clipquest.entity.VideoView;
import com.filahi.springboot.clipquest.enumeration.LikeType;
import com.filahi.springboot.clipquest.repository.VideoLikeRepository;
import com.filahi.springboot.clipquest.repository.VideoRepository;
import com.filahi.springboot.clipquest.repository.VideoViewRepository;
import com.filahi.springboot.clipquest.response.VideoLikeResponse;
import com.filahi.springboot.clipquest.service.VideoInteractionService;
import com.filahi.springboot.clipquest.util.FindAuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@Transactional
public class VideoInteractionServiceImpl implements VideoInteractionService {
    private final VideoRepository videoRepository;
    private final VideoLikeRepository videoLikeRepository;
    private final VideoViewRepository videoViewRepository;
    private final FindAuthenticatedUser findAuthenticatedUser;

    public VideoInteractionServiceImpl(VideoRepository videoRepository, VideoLikeRepository videoLikeRepository, VideoViewRepository videoViewRepository, FindAuthenticatedUser findAuthenticatedUser) {
        this.videoRepository = videoRepository;
        this.videoLikeRepository = videoLikeRepository;
        this.videoViewRepository = videoViewRepository;
        this.findAuthenticatedUser = findAuthenticatedUser;
    }


    @Override
    public VideoLikeResponse toggleLike(long videoId, LikeType likeType) {
        User user = this.findAuthenticatedUser.getAuthenticatedUser();

        Video video = this.videoRepository.findById(videoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));

        Optional<VideoLike> existingLike = this.videoLikeRepository.findByUserAndVideo(user, video);

        if (existingLike.isPresent()) {
            VideoLike videoLike = existingLike.get();

            if(videoLike.getType().equals(likeType)) {
                this.videoLikeRepository.delete(videoLike);
                updateCachedCounts(video);

                return new VideoLikeResponse(
                        null,
                        video.getCachedLikesCount(),
                        video.getCachedDislikesCount()
                );
            }
            else{
                videoLike.setType(likeType);
                this.videoLikeRepository.save(videoLike);
                updateCachedCounts(video);

                return new VideoLikeResponse(
                        likeType,
                        video.getCachedLikesCount(),
                        video.getCachedDislikesCount()
                );
            }
        }
        else {
            VideoLike newVideoLike = new VideoLike();
            newVideoLike.setUser(user);
            newVideoLike.setVideo(video);
            newVideoLike.setType(likeType);
            this.videoLikeRepository.save(newVideoLike);

            updateCachedCounts(video);
            return new VideoLikeResponse(
                    likeType,
                    video.getCachedLikesCount(),
                    video.getCachedDislikesCount()
            );
        }
    }


    @Override
    public void registerView(long videoId, String ipAddress) {
//        User user = this.findAuthenticatedUser.getAuthenticatedUser();

        Video video = this.videoRepository.findById(videoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));

        boolean alreadyViewed = false;

/*        if(user != null){
            alreadyViewed = this.videoViewRepository.existsByUserAndVideo(user, video);
        }
        else{*/
            LocalDateTime oneDayAgo =  LocalDateTime.now().minusDays(1);
            alreadyViewed = this.videoViewRepository.existsByIpAddressAndVideoAndViewedAtAfter(
                    ipAddress, video, oneDayAgo
            );
//        }

        if(!alreadyViewed){
            VideoView videoView = new VideoView();
            videoView.setVideo(video);
//            videoView.setUser(user);
            videoView.setIpAddress(ipAddress);
            videoViewRepository.save(videoView);

            video.setCachedViewsCount(video.getCachedViewsCount() + 1);
            this.videoRepository.save(video);
        }
    }

    @Override
    public LikeType getUserLikeStatus(long videoId) {
        User user = this.findAuthenticatedUser.getAuthenticatedUser();

        Video video = this.videoRepository.findById(videoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));

        return this.videoLikeRepository.findByUserAndVideo(user, video)
                .map(VideoLike::getType)
                .orElse(null);
    }



    private void updateCachedCounts(Video video) {
        video.setCachedLikesCount((int) this.videoLikeRepository.countByVideoAndType(video, LikeType.LIKE));
        video.setCachedDislikesCount((int) this.videoLikeRepository.countByVideoAndType(video, LikeType.DISLIKE));
        this.videoRepository.save(video);
    }
}
