package com.filahi.springboot.clipquest.entity;


import com.filahi.springboot.clipquest.enumeration.LikeType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(
        name = "video_likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "video_id"})
)
public class VideoLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",  nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id",   nullable = false)
    private Video video;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LikeType type;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VideoLike videoLike = (VideoLike) o;
        return id == videoLike.id && Objects.equals(user, videoLike.user) && Objects.equals(video, videoLike.video) && type == videoLike.type && Objects.equals(createdAt, videoLike.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, video, type, createdAt);
    }

    public VideoLike() {
    }

    public VideoLike(long id, User user, Video video, LikeType type) {
        this.id = id;
        this.user = user;
        this.video = video;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public LikeType getType() {
        return type;
    }

    public void setType(LikeType type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
