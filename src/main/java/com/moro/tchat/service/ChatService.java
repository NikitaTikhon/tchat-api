package com.moro.tchat.service;

import com.moro.tchat.dto.response.ChatResponse;
import com.moro.tchat.entity.Chat;

import java.util.List;

public interface ChatService {

    List<ChatResponse> findChatsByUserIdentifier(String identifier, Long userId);

    List<Long> findChatsWithUnreadMessages(Long userId);

    ChatResponse removeChat(Long chatId);
    Chat getReferenceChat(Long id);

}
