package com.filahi.springboot.clipquest.service;


import com.filahi.springboot.clipquest.entity.Authority;
import com.filahi.springboot.clipquest.entity.User;
import com.filahi.springboot.clipquest.service.impl.JwtServiceImpl;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {


    @InjectMocks
    private JwtServiceImpl jwtService;



    private User user;


    @BeforeEach
    public void setup(){
        ReflectionTestUtils.setField(jwtService, "SECRET", "VGhpc0lzQVN1cGVyU2VjcmV0S2V5VGhpc0lzQVN1cGVyU2VjcmV0S2V5");
        ReflectionTestUtils.setField(jwtService, "JWT_EXPIRATION", 86400000L);

        user = new User();
        user.setId(1L);
        user.setFirstName("mario");
        user.setLastName("rossi");
        user.setEmail("mario.rossi@example.com");
        user.setAuthorities(List.of(new Authority("ROLE_USER")));
    }

    @DisplayName("Should generate a token successfully")
    @Test
    public void generateTokenSuccessfully() {
        String result = jwtService.generateToken(new HashMap<>(), user);

        assertNotNull(result);
        assertFalse(result.isBlank());
    }

    @DisplayName("Should have the correct username in the token")
    @Test
    public void generateTokenGtUsernameFromToken(){
        String result = jwtService.generateToken(new HashMap<>(), user);
        String username = jwtService.extractUsername(result);

        assertEquals(user.getEmail(), username);
    }

    @DisplayName("Should have the authorities in the token")
    @Test
    public void generateTokenGetAuthoritiesFromToken(){
        String result = jwtService.generateToken(new HashMap<>(), user);

        Claims claims = ReflectionTestUtils.invokeMethod(jwtService, "getClaimsFromToken", result);
        List<String> authorities = claims.get("authorities", List.class);

        assertNotNull(authorities);
        assertNotNull(claims);
        assertTrue(authorities.contains("ROLE_USER"));
    }

    @DisplayName("Should set the expiration for the token")
    @Test
    public void generateTokenExpirationIsSetTest() {
        String result = jwtService.generateToken(new HashMap<>(), user);

        Claims claims = ReflectionTestUtils.invokeMethod(jwtService, "getClaimsFromToken", result);
        Date expiration = ReflectionTestUtils.invokeMethod(jwtService, "extractExpiration", result);

        assertNotNull(claims);
        assertEquals(expiration, claims.getExpiration());
    }
}
