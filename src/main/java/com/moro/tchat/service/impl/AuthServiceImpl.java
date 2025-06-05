package com.moro.tchat.service.impl;

import com.moro.tchat.dto.request.LoginRequest;
import com.moro.tchat.dto.request.SignupRequest;
import com.moro.tchat.dto.response.LoginResponse;
import com.moro.tchat.entity.User;
import com.moro.tchat.entity.VerificationToken;
import com.moro.tchat.event.OnRegistrationCompleteEvent;
import com.moro.tchat.exception.EntityExistException;
import com.moro.tchat.exception.VerificationException;
import com.moro.tchat.mapper.AuthMapper;
import com.moro.tchat.repository.UserRepository;
import com.moro.tchat.repository.VerificationTokenRepository;
import com.moro.tchat.service.AuthService;
import com.moro.tchat.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    private final AuthMapper authMapper;

    private final ApplicationEventPublisher eventPublisher;

    private final AuthenticationManager authenticationManager;

    private JwtUtil jwtUtil;

    @Override
    public void signup(SignupRequest signupRequest, HttpServletRequest request) {
        User user = registerNewUserAccount(signupRequest);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(),
                request.getServerName() + ":" + request.getServerPort()));
    }

    @Override
    public User registerNewUserAccount(SignupRequest signupRequest) {
        List<User> existsUsers = userRepository.findAllByEmailOrUsername(signupRequest.getEmail(), signupRequest.getUsername());

        if (existsUsers.isEmpty()) {
            User user = authMapper.signupRequestToUser(signupRequest);
            userRepository.save(user);
            return user;
        } else if (existsUsers.size() == 2 || existsUsers.getFirst().getEmail().equals(signupRequest.getEmail()) &&
                existsUsers.getFirst().getUsername().equals(signupRequest.getUsername())) {
            throw new EntityExistException("Registration error", HttpStatus.CONFLICT, Map.of(
                    "email", "This email is already in use",
                    "username", "This username is already in use"
            ));
        } else if (existsUsers.getFirst().getEmail().equals(signupRequest.getEmail())) {
            throw new EntityExistException("Registration error", HttpStatus.CONFLICT, Map.of(
                    "email", "This email is already in use"
            ));
        } else {
            throw new EntityExistException("Registration error", HttpStatus.CONFLICT, Map.of(
                    "username", "This username is already in use"
            ));
        }
    }

    @Override
    public void saveRegisteredUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);
    }

    @Override
    public void confirmRegistration(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new VerificationException("Invalid token", HttpStatus.UNAUTHORIZED));

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new VerificationException("Verification expired", HttpStatus.UNAUTHORIZED);
        }

        user.setEnabled(true);
        this.saveRegisteredUser(user);
    }


    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        User user = User.builder()
                .email(authentication.getName())
                .build();

        return LoginResponse.builder()
                .token(jwtUtil.createToken(user))
                .build();
    }

}
