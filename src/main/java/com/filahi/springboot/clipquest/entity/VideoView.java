package com.filahi.springboot.clipquest.entity;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "video_views")
public class VideoView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime viewedAt;

    @Column(length = 45)
    private String ipAddress;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VideoView videoView = (VideoView) o;
        return id == videoView.id && Objects.equals(user, videoView.user) && Objects.equals(video, videoView.video) && Objects.equals(viewedAt, videoView.viewedAt) && Objects.equals(ipAddress, videoView.ipAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, video, viewedAt, ipAddress);
    }

    public VideoView() {
    }

    public VideoView(long id, User user, Video video, String ipAddress, LocalDateTime viewedAt) {
        this.id = id;
        this.user = user;
        this.video = video;
        this.ipAddress = ipAddress;
        this.viewedAt = viewedAt;
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

    public LocalDateTime getViewedAt() {
        return viewedAt;
    }

    public void setViewedAt(LocalDateTime viewedAt) {
        this.viewedAt = viewedAt;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
