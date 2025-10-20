package com.filahi.springboot.clipquest.repository;

import com.filahi.springboot.clipquest.entity.User;
import com.filahi.springboot.clipquest.entity.Video;
import com.filahi.springboot.clipquest.entity.VideoLike;
import com.filahi.springboot.clipquest.enumeration.LikeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface VideoLikeRepository extends JpaRepository<VideoLike, Long> {
    Optional<VideoLike> findByUserAndVideo(User user, Video video);

    boolean existsByUserAndVideo(User user, Video video);

    long countByVideoAndType(Video video, LikeType type);

    @Modifying
    @Query("DELETE FROM VideoLike vl WHERE vl.user = :user AND vl.video = :video")
    void deleteByUserAndVideo(User user, Video video);
}
