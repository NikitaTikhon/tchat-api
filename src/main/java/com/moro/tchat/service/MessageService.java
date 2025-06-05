package com.moro.tchat.service;

import com.moro.tchat.dto.request.MessageRequest;
import com.moro.tchat.dto.ChatMessages;
import com.moro.tchat.dto.response.ChatResponse;
import com.moro.tchat.dto.response.MessageResponse;

import java.util.List;
import java.util.Map;

public interface MessageService {

    List<MessageResponse> getMessagesByChatId(Long chatId);

    MessageResponse saveMessage(MessageRequest messageRequest, String email);

    List<Long> updateMessagesIsRead(ChatMessages unreadMessagesRequest);

    Map<Long, ChatResponse> createNewChatAndSaveMessage(MessageRequest messageRequest, String email);

    MessageResponse removeMessage(MessageRequest messageRequest);

}
