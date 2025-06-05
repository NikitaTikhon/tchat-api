package com.moro.tchat.service;

import com.moro.tchat.dto.request.LoginRequest;
import com.moro.tchat.dto.request.SignupRequest;
import com.moro.tchat.dto.response.LoginResponse;
import com.moro.tchat.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    void signup(SignupRequest signupRequest, HttpServletRequest request);

    User registerNewUserAccount(SignupRequest signupRequest);

    void saveRegisteredUser(User user);

    void createVerificationToken(User user, String token);

    void confirmRegistration(String token);

    LoginResponse login(LoginRequest loginRequest);

}
