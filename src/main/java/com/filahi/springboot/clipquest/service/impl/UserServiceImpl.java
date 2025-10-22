package com.filahi.springboot.clipquest.service.impl;

import com.filahi.springboot.clipquest.entity.Authority;
import com.filahi.springboot.clipquest.entity.User;
import com.filahi.springboot.clipquest.repository.UserRepository;
import com.filahi.springboot.clipquest.response.UserResponse;
import com.filahi.springboot.clipquest.service.UserService;
import com.filahi.springboot.clipquest.util.FindAuthenticatedUser;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FindAuthenticatedUser findAuthenticatedUser;

    public UserServiceImpl(UserRepository userRepository, FindAuthenticatedUser findAuthenticatedUser) {
        this.userRepository = userRepository;
        this.findAuthenticatedUser = findAuthenticatedUser;
    }



    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserInfo() {
        User user = this.findAuthenticatedUser.getAuthenticatedUser();

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getAge(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAuthorities().stream().map(auth -> (Authority) auth).toList()
        );
    }

    @Override
    @Transactional
    public void deleteUser() {
        User user  = this.findAuthenticatedUser.getAuthenticatedUser();
        this.userRepository.deleteById(user.getId());
    }
}
