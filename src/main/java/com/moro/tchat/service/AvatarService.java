package com.moro.tchat.service;

import com.moro.tchat.dto.response.AvatarResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AvatarService {

    AvatarResponse saveAvatar(MultipartFile avatar, Long userId);

    AvatarResponse getAvatar(Long userId);

    void removeAvatar(String publicId, Long userId);

}
