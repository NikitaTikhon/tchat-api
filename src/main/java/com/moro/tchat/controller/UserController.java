package com.moro.tchat.controller;

import com.moro.tchat.dto.response.UserResponse;
import com.moro.tchat.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {

    private UserService userService;

    @GetMapping("/v1/user/authenticated")
    public ResponseEntity<UserResponse> getAuthUser(@AuthenticationPrincipal UserDetails authUser) {
        UserResponse userResponse = userService.getAuthUser(authUser.getUsername());
        return ResponseEntity.ok(userResponse);
    }

}
