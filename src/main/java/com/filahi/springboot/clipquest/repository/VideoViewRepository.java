package com.filahi.springboot.clipquest.repository;

import com.filahi.springboot.clipquest.entity.User;
import com.filahi.springboot.clipquest.entity.Video;
import com.filahi.springboot.clipquest.entity.VideoView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;


@Repository
public interface VideoViewRepository extends JpaRepository<VideoView, Long> {
    boolean existsByIpAddressAndVideoAndViewedAtAfter(
            String ipAddress, Video video, LocalDateTime viewedAt);
}
