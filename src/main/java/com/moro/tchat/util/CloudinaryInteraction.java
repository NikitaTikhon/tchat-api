package com.moro.tchat.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.moro.tchat.config.constant.AvatarConstant;
import com.moro.tchat.config.constant.CloudinaryConstant;
import com.moro.tchat.exception.AvatarException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class CloudinaryInteraction {

    private Cloudinary cloudinary;

    public Map<String, String> saveAvatar(File file, Long userId) {
        try {
            Map uploadedFile = cloudinary.uploader().upload(file,
                    ObjectUtils.asMap(
                            "folder", CloudinaryConstant.AVATAR_PATH.formatted(userId),
                            "transformation", new Transformation().quality(100)));

            HashMap<String, String> fileInfo = new HashMap<>();

            fileInfo.put("url", String.valueOf(uploadedFile.get("secure_url")));
            String publicIdPath = String.valueOf(uploadedFile.get("public_id"));
            fileInfo.put("publicId", publicIdPath.substring(publicIdPath.lastIndexOf("/") + 1));

            return fileInfo;
        } catch (IOException e) {
            throw new AvatarException(AvatarConstant.AVATAR_NOT_SAVED, e);
        }
    }

    public void removeAvatar(String publicId, Long userId) {
        try {
            cloudinary.uploader().destroy(CloudinaryConstant.AVATAR_DELETE_PATH.formatted(userId, publicId),
                    ObjectUtils.asMap("invalidate", true));
        } catch (IOException e) {
            throw new AvatarException(AvatarConstant.AVATAR_NOT_DELETED, e);
        }
    }

}
