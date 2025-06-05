package com.moro.tchat.mapper;

import com.moro.tchat.dto.response.UserResponse;
import com.moro.tchat.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse userToUserResponse(User user);

    List<UserResponse> usersToUserResponses(List<User> users);

}
