package com.moro.tchat.service.impl;

import com.moro.tchat.dto.response.ChatResponse;
import com.moro.tchat.entity.Avatar;
import com.moro.tchat.entity.Chat;
import com.moro.tchat.entity.User;
import com.moro.tchat.exception.EntityNotFoundException;
import com.moro.tchat.repository.ChatRepository;
import com.moro.tchat.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {

    private ChatRepository chatRepository;

    @Override
    @Transactional
    public List<ChatResponse> findChatsByUserIdentifier(String identifier, Long userId) {
        List<Chat> chats = chatRepository.findByUserIdentifier(identifier, userId);

        List<ChatResponse> userChatRespons = chats.stream().map(chat -> {
            User userReceiver;
            if (Objects.equals(chat.getUserCreator().getId(), userId)) {
                userReceiver = chat.getUserReceiver();
            } else {
                userReceiver = chat.getUserCreator();
            }

            return ChatResponse.builder()
                    .chatId(chat.getId())
                    .receiverUserId(userReceiver.getId())
                    .email(userReceiver.getEmail())
                    .username(userReceiver.getUsername())
                    .avatarURL(Optional.ofNullable(userReceiver.getAvatar()).map(Avatar::getAvatarURL).orElse(null))
                    .lastMessageId(chat.getLastMessage().getId())
                    .lastMessage(chat.getLastMessage().getContent())
                    .lastMessageTimeSend(chat.getLastMessage().getTimeSend())
                    .lastMessageIsRead(chat.getLastMessage().getIsRead())
                    .numberUnreadMessages(chat.getMessages().stream().filter(message ->
                        message.getUserReceiver().getId().equals(userId) && !message.getIsRead()
                    ).count())
                    .build();
        }).toList();

        return userChatRespons;
    }

    @Override
    public List<Long> findChatsWithUnreadMessages(Long userId) {
        return chatRepository.findChatsWithUnreadMessages(userId);
    }

    @Override
    @Transactional
    public ChatResponse removeChat(Long chatId) {
        Chat chat = chatRepository.getReferenceById(chatId);
        chatRepository.delete(chat);

        return ChatResponse.builder()
                .chatId(chat.getId())
                .senderUserId(chat.getUserCreator().getId())
                .receiverUserId(chat.getUserReceiver().getId())
                .build();
    }

    @Override
    public Chat getReferenceChat(Long id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chat not found", HttpStatus.NOT_FOUND));
    }

}
