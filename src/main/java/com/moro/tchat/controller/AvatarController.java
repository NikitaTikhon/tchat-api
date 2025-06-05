package com.moro.tchat.controller;

import com.moro.tchat.config.security.UserDetailsImpl;
import com.moro.tchat.dto.response.AvatarResponse;
import com.moro.tchat.service.AvatarService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AvatarController {

    private AvatarService avatarService;

    @PostMapping("/v1/avatar")
    public ResponseEntity<AvatarResponse> saveAvatar(@RequestParam("avatar") MultipartFile avatar, @AuthenticationPrincipal UserDetailsImpl authUser) {
        AvatarResponse avatarResponse = avatarService.saveAvatar(avatar, authUser.getId());
        return ResponseEntity.ok(avatarResponse);
    }

    @GetMapping("/v1/avatar")
    public ResponseEntity<AvatarResponse> getAvatar(@AuthenticationPrincipal UserDetailsImpl authUser) {
        AvatarResponse avatarResponse = avatarService.getAvatar(authUser.getId());
        return ResponseEntity.ok(avatarResponse);
    }

    @DeleteMapping("/v1/avatar/{publicId}")
    public ResponseEntity<?> deleteAvatar(@PathVariable("publicId") String publicId, @AuthenticationPrincipal UserDetailsImpl authUser) {
        avatarService.removeAvatar(publicId, authUser.getId());
        return ResponseEntity.ok().build();
    }

}
