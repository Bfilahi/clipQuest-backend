package com.filahi.springboot.clipquest.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Authority implements GrantedAuthority {
    private String authority;
}
