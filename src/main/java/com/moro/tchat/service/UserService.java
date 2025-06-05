package com.moro.tchat.service;

import com.moro.tchat.dto.response.FoundChatResponse;
import com.moro.tchat.dto.response.UserResponse;
import com.moro.tchat.entity.User;

import java.util.List;

public interface UserService {

    UserResponse getAuthUser(String email);

    List<FoundChatResponse> searchNewChatsByUserIdentifier(String identifier, Long userId);

    User getReferenceAuthUser(String email);

    User getReferenceUser(Long userId);

}
