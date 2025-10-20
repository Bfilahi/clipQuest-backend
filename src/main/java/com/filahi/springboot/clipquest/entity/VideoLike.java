package com.filahi.springboot.clipquest.entity;


import com.filahi.springboot.clipquest.enumeration.LikeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(
        name = "video_likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "video_id"})
)
@Data
@AllArgsConstructor
@NoArgsConstructor
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

//    @Table(uniqueConstraints = {
//            @UniqueConstraint(columnNames = {"user_id", "video_id"})
//    })
//    public static class TableDefinition{}
}
