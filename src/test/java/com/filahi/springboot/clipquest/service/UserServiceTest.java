package com.filahi.springboot.clipquest.service;


import com.filahi.springboot.clipquest.entity.User;
import com.filahi.springboot.clipquest.repository.UserRepository;
import com.filahi.springboot.clipquest.response.UserResponse;
import com.filahi.springboot.clipquest.service.impl.UserServiceImpl;
import com.filahi.springboot.clipquest.util.FindAuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private FindAuthenticatedUser findAuthenticatedUser;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;


    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("mario");
        user.setLastName("rossi");
        user.setEmail("mario.rossi@example.com");
        user.setAge(29);
        user.setPhoneNumber("1234567890");
        user.setProfilePicture("Profile-Picture");
        user.setAuthorities(List.of());
    }


    @DisplayName("Should return user info successfully")
    @Test
    public void getUserInfoTest() {
        when(findAuthenticatedUser.getAuthenticatedUser()).thenReturn(user);

        UserResponse result = userService.getUserInfo();

        assertAll(
                () -> assertEquals(user.getId(), result.id()),
                () -> assertEquals(user.getFirstName(), result.firstName()),
                () -> assertEquals(user.getLastName(), result.lastName()),
                () -> assertEquals(user.getEmail(), result.email()),
                () -> assertEquals(user.getAge(), result.age()),
                () -> assertEquals(user.getPhoneNumber(), result.phoneNumber()),
                () -> assertEquals(user.getProfilePicture(), result.profilePicture())
        );
    }

    @DisplayName("Should delete user successfully")
    @Test
    public void deleteUserTest() {
        when(findAuthenticatedUser.getAuthenticatedUser()).thenReturn(user);

        userService.deleteUser();

        verify(userRepository).deleteById(user.getId());
    }
}
