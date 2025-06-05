package com.moro.tchat.controller;

import com.moro.tchat.dto.request.LoginRequest;
import com.moro.tchat.dto.request.SignupRequest;
import com.moro.tchat.dto.response.LoginResponse;
import com.moro.tchat.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    @PostMapping(value = "/v1/signup")
    public ResponseEntity<?> signup(@ModelAttribute SignupRequest signupRequest, HttpServletRequest request) {
        authService.signup(signupRequest, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/v1/registrationConfirm")
    public ModelAndView confirmRegistration(@RequestParam("token") String token) {
        authService.confirmRegistration(token);
        ModelAndView modelAndView = new ModelAndView("email-verification-successful");
        modelAndView.addObject("link", "http://localhost:5173/login");
        return modelAndView;
    }

    @PostMapping("/v1/login")
    public ResponseEntity<?> login(@ModelAttribute LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

}


