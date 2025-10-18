package com.filahi.springboot.clipquest.repository;

import com.filahi.springboot.clipquest.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
