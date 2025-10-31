package com.filahi.springboot.clipquest.repository;

import com.filahi.springboot.clipquest.entity.Comment;
import com.filahi.springboot.clipquest.entity.User;
import com.filahi.springboot.clipquest.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByVideo(Video video);
    List<Comment> findByVideoAndUser(Video video, User user);
    Optional<Comment> findByUserAndId(User user, long commentId);
}
