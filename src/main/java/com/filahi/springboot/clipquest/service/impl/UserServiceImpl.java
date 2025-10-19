package com.filahi.springboot.clipquest.service.impl;

import com.filahi.springboot.clipquest.entity.Authority;
import com.filahi.springboot.clipquest.entity.User;
import com.filahi.springboot.clipquest.repository.UserRepository;
import com.filahi.springboot.clipquest.response.UserResponse;
import com.filahi.springboot.clipquest.service.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserInfo() {
        User user = getAuthenticatedUser();

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAuthorities().stream().map(auth -> (Authority) auth).toList()
        );
    }

    @Override
    @Transactional
    public void deleteUser() {
        User user = getAuthenticatedUser();

        this.userRepository.deleteById(user.getId());
    }


    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated() ||
            authentication.getPrincipal().equals("anonymousUser")){

            throw new AccessDeniedException("Authentication required");
        }

        return (User) authentication.getPrincipal();
    }
}
