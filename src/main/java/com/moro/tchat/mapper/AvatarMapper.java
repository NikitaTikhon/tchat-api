package com.moro.tchat.mapper;

import com.moro.tchat.dto.response.AvatarResponse;
import com.moro.tchat.entity.Avatar;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AvatarMapper {

    @Mapping(target = "userId", source = "userAvatar.id")
    AvatarResponse avatarToAvatarResponse(Avatar avatar);

}
