package com.moro.tchat.service.impl;

import com.moro.tchat.dto.response.FoundChatResponse;
import com.moro.tchat.dto.response.UserResponse;
import com.moro.tchat.entity.Avatar;
import com.moro.tchat.entity.User;
import com.moro.tchat.exception.EntityNotFoundException;
import com.moro.tchat.mapper.UserMapper;
import com.moro.tchat.repository.ChatRepository;
import com.moro.tchat.repository.UserRepository;
import com.moro.tchat.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private ChatRepository chatRepository;

    private UserMapper userMapper;

    @Override
    public UserResponse getAuthUser(String email) {
        User authUser = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User with %s has been not found".formatted(email), HttpStatus.NOT_FOUND));

        return userMapper.userToUserResponse(authUser);
    }

    @Override
    public List<FoundChatResponse> searchNewChatsByUserIdentifier(String identifier, Long userId) {
        List<Long> userIds = chatRepository.findNotCreatedYetChats(userId);
        List<User> users;
        if (userIds.isEmpty()) {
            users = userRepository.findAllByEmailStartsWithIgnoreCaseOrUsernameStartsWithIgnoreCaseAndEnabledTrue(identifier, identifier);
        } else {
            users = userRepository.findAllByEmailStartsWithIgnoreCaseAndIdIsNotInOrUsernameStartsWithIgnoreCaseAndEnabledTrueAndIdIsNotIn(identifier, userIds, identifier, userIds);
        }

        return users.stream().map(user ->
            FoundChatResponse.builder()
                    .receiverUserId(user.getId())
                    .username(user.getUsername())
                    .avatarURL(Optional.ofNullable(user.getAvatar()).map(Avatar::getAvatarURL).orElse(null))
                    .build()
        ).toList();
    }

    @Override
    public User getReferenceAuthUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User with %s has been not found".formatted(email), HttpStatus.NOT_FOUND));
    }

    @Override
    public User getReferenceUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User %s has been not found".formatted(userId), HttpStatus.NOT_FOUND));
    }

}
