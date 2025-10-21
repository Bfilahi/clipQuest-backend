package com.filahi.springboot.clipquest.repository;

import com.filahi.springboot.clipquest.entity.User;
import com.filahi.springboot.clipquest.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByUser(User user);
}
