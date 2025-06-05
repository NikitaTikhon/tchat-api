package com.moro.tchat.service.impl;


import com.moro.tchat.dto.response.AvatarResponse;
import com.moro.tchat.entity.Avatar;
import com.moro.tchat.mapper.AvatarMapper;
import com.moro.tchat.repository.AvatarRepository;
import com.moro.tchat.service.AvatarService;
import com.moro.tchat.service.UserService;
import com.moro.tchat.util.CloudinaryInteraction;
import com.moro.tchat.util.FileUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AvatarServiceImpl implements AvatarService {

    private UserService userService;

    private AvatarRepository avatarRepository;

    private AvatarMapper avatarMapper;

    private CloudinaryInteraction cloudinaryInteraction;

    @Override
    @Transactional
    public AvatarResponse saveAvatar(MultipartFile avatar, Long userId) {
        File file = FileUtil.multipartToFile(avatar);

        Optional<Avatar> avatarExistOpt = avatarRepository.findByUserAvatarId(userId);
        if (avatarExistOpt.isPresent()) {
            Avatar avatarExist = avatarExistOpt.get();
            cloudinaryInteraction.removeAvatar(avatarExist.getPublicId(), userId);
            Map<String, String> fileInfo = cloudinaryInteraction.saveAvatar(file, userId);

            avatarExist.setPublicId(fileInfo.get("publicId"));
            avatarExist.setAvatarURL(fileInfo.get("url"));
            avatarRepository.flush();

            return avatarMapper.avatarToAvatarResponse(avatarExist);
        }

        Map<String, String> fileInfo = cloudinaryInteraction.saveAvatar(file, userId);

        Avatar avatarEntity = Avatar.builder()
                .avatarURL(fileInfo.get("url"))
                .publicId(fileInfo.get("publicId"))
                .userAvatar(userService.getReferenceUser(userId))
                .build();

        avatarRepository.save(avatarEntity);

        return avatarMapper.avatarToAvatarResponse(avatarEntity);
    }

    @Override
    public AvatarResponse getAvatar(Long userId) {
        Avatar avatar = avatarRepository.findByUserAvatarId(userId)
                .orElse(null);

        return avatarMapper.avatarToAvatarResponse(avatar);
    }

    @Override
    @Transactional
    public void removeAvatar(String publicId, Long userId) {
        Long isRemoved = avatarRepository.removeByPublicIdAndUserAvatarId(publicId, userId);
        if (isRemoved == 1) {
            cloudinaryInteraction.removeAvatar(publicId, userId);
        }
    }

    @Autowired
    public void setMissionService(UserService userService) {
        this.userService = userService;
    }

}
