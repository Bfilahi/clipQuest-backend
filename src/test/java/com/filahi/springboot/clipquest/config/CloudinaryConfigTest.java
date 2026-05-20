package com.filahi.springboot.clipquest.config;


import com.cloudinary.Cloudinary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CloudinaryConfigTest {

    @InjectMocks
    private CloudinaryConfig cloudinaryConfig;


    private final String cloudName = "Mock-cloudinary-name";
    private final String apiKey = "mock-cloudinary-api-key";
    private final String apiSecret = "mock-cloudinary-api-secret";


    @BeforeEach
    public void setup(){
        ReflectionTestUtils.setField(cloudinaryConfig, "cloudinaryName", cloudName);
        ReflectionTestUtils.setField(cloudinaryConfig, "apiKey", apiKey);
        ReflectionTestUtils.setField(cloudinaryConfig, "apiSecret", apiSecret);
    }


    @Test
    void shouldLoadCloudinaryProperties(){
        Cloudinary result = cloudinaryConfig.cloudinary();

        assertEquals(cloudName, result.config.cloudName);
        assertEquals(apiKey, result.config.apiKey);
        assertEquals(apiSecret, result.config.apiSecret);
    }
}
