package com.moro.tchat.mapper.annotation;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PasswordEncoderMapper {

    private final PasswordEncoder passwordEncoder;

    @EncodingMapping
    public String encode(String password) {
        return passwordEncoder.encode(password);
    }

}