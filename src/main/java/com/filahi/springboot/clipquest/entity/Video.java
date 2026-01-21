package com.filahi.springboot.clipquest.entity;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "videos")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String thumbnailPath;

    @Column(nullable = false)
    private String CloudinaryPublicId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VideoLike> likes;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VideoView> views;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private int cachedViewsCount = 0;
    private int cachedLikesCount = 0;
    private int cachedDislikesCount = 0;

    public Video() {
    }

    public Video(long id, String title, String description, String filePath, String thumbnailPath, String cloudinaryPublicId, User user, int cachedLikesCount, int cachedDislikesCount, int cachedViewsCount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.filePath = filePath;
        this.thumbnailPath = thumbnailPath;
        CloudinaryPublicId = cloudinaryPublicId;
        this.user = user;
        this.cachedLikesCount = cachedLikesCount;
        this.cachedDislikesCount = cachedDislikesCount;
        this.cachedViewsCount = cachedViewsCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getCloudinaryPublicId() {
        return CloudinaryPublicId;
    }

    public void setCloudinaryPublicId(String cloudinaryPublicId) {
        CloudinaryPublicId = cloudinaryPublicId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<VideoLike> getLikes() {
        return likes;
    }

    public void setLikes(List<VideoLike> likes) {
        this.likes = likes;
    }

    public List<VideoView> getViews() {
        return views;
    }

    public void setViews(List<VideoView> views) {
        this.views = views;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getCachedViewsCount() {
        return cachedViewsCount;
    }

    public void setCachedViewsCount(int cachedViewsCount) {
        this.cachedViewsCount = cachedViewsCount;
    }

    public int getCachedLikesCount() {
        return cachedLikesCount;
    }

    public void setCachedLikesCount(int cachedLikesCount) {
        this.cachedLikesCount = cachedLikesCount;
    }

    public int getCachedDislikesCount() {
        return cachedDislikesCount;
    }

    public void setCachedDislikesCount(int cachedDislikesCount) {
        this.cachedDislikesCount = cachedDislikesCount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return id == video.id && cachedViewsCount == video.cachedViewsCount && cachedLikesCount == video.cachedLikesCount && cachedDislikesCount == video.cachedDislikesCount && Objects.equals(title, video.title) && Objects.equals(description, video.description) && Objects.equals(filePath, video.filePath) && Objects.equals(thumbnailPath, video.thumbnailPath) && Objects.equals(CloudinaryPublicId, video.CloudinaryPublicId) && Objects.equals(user, video.user) && Objects.equals(likes, video.likes) && Objects.equals(views, video.views) && Objects.equals(comments, video.comments) && Objects.equals(createdAt, video.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, filePath, thumbnailPath, CloudinaryPublicId, user, likes, views, comments, createdAt, cachedViewsCount, cachedLikesCount, cachedDislikesCount);
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", filePath='" + filePath + '\'' +
                ", thumbnailPath='" + thumbnailPath + '\'' +
                ", CloudinaryPublicId='" + CloudinaryPublicId + '\'' +
                ", user=" + user +
                ", createdAt=" + createdAt +
                ", cachedViewsCount=" + cachedViewsCount +
                ", cachedLikesCount=" + cachedLikesCount +
                ", cachedDislikesCount=" + cachedDislikesCount +
                '}';
    }
}
