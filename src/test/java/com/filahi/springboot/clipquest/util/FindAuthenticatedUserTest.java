package com.filahi.springboot.clipquest.util;


import com.filahi.springboot.clipquest.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindAuthenticatedUserTest {

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private FindAuthenticatedUserImpl findAuthenticatedUser;


    @DisplayName("Should return authenticated user successfully")
    @Test
    public void getAuthenticatedUserTest() {
        User user = new User();
        user.setFirstName("mario");
        user.setLastName("rossi");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);

        User result = findAuthenticatedUser.getAuthenticatedUser();

        assertEquals(user, result);
    }

    @DisplayName("Should throw exception when authentication is null")
    @Test
    public void getAuthenticatedUserAuthenticationNullTest() {
        when(securityContext.getAuthentication()).thenReturn(null);

        SecurityContextHolder.setContext(securityContext);

        assertThrows(AccessDeniedException.class, () -> findAuthenticatedUser.getAuthenticatedUser());
    }

    @DisplayName("Should throw exception when user is not authenticated")
    @Test
    public void getAuthenticatedUserUserNotAuthenticatedTest() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.isAuthenticated()).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> findAuthenticatedUser.getAuthenticatedUser());
    }

    @DisplayName("Should throw exception when principal is anonymousUser")
    @Test
    public void getAuthenticatedUserAnonymousUserTest() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("anonymousUser");

        assertThrows(AccessDeniedException.class, () -> findAuthenticatedUser.getAuthenticatedUser());
    }
}
