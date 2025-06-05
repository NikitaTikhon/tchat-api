package com.moro.tchat.service.impl;

import com.moro.tchat.dto.request.MessageRequest;
import com.moro.tchat.dto.ChatMessages;
import com.moro.tchat.dto.response.ChatResponse;
import com.moro.tchat.dto.response.MessageResponse;
import com.moro.tchat.entity.Avatar;
import com.moro.tchat.entity.Chat;
import com.moro.tchat.entity.Message;
import com.moro.tchat.entity.User;
import com.moro.tchat.mapper.MessageMapper;
import com.moro.tchat.repository.MessageRepository;
import com.moro.tchat.service.ChatService;
import com.moro.tchat.service.MessageService;
import com.moro.tchat.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private UserService userService;

    private ChatService chatService;

    private MessageRepository messageRepository;

    private MessageMapper messageMapper;

    @Override
    @Transactional
    public List<MessageResponse> getMessagesByChatId(Long chatId) {
        List<Message> messages = messageRepository.findAllByChatIdOrderByTimeSendAsc(chatId);

        return messageMapper.messagesToMessageResponses(messages);
    }

    @Override
    @Transactional
    public MessageResponse saveMessage(MessageRequest messageRequest, String email) {
        Message message = messageMapper.messageRequestToMessage(messageRequest);
        User userSenderRef = userService.getReferenceAuthUser(email);
        User userReceiverRef = userService.getReferenceUser(messageRequest.getUserReceiverId());
        message.setUserSender(userSenderRef);
        message.setUserReceiver(userReceiverRef);
        message.setTimeSend(LocalDateTime.now());
        message.setIsRead(messageRequest.getIsRead());

        Optional.ofNullable(messageRequest.getChatId()).ifPresent(chatId -> {
            Chat chat = chatService.getReferenceChat(chatId);
            chat.setLastMessage(message);
            message.setChat(chat);
            message.setLastChatMessage(chat);
            messageRepository.save(message);
        });

        return messageMapper.messageToMessageResponse(message);
    }

    @Override
    @Transactional
    public List<Long> updateMessagesIsRead(ChatMessages unreadMessagesRequest) {
        List<Message> messages = messageRepository.getAllByIdIn(unreadMessagesRequest.getMessageIds());
        List<Message> updateMessages = messages.stream()
                .peek(message -> message.setIsRead(true))
                .toList();

        return messageRepository.saveAll(updateMessages).stream()
                .map(Message::getId)
                .toList();
    }

    @Override
    @Transactional
    public Map<Long, ChatResponse> createNewChatAndSaveMessage(MessageRequest messageRequest, String email) {
        Message message = messageMapper.messageRequestToMessage(messageRequest);
        User userSenderRef = userService.getReferenceAuthUser(email);
        User userReceiverRef = userService.getReferenceUser(messageRequest.getUserReceiverId());
        message.setUserSender(userSenderRef);
        message.setUserReceiver(userReceiverRef);
        message.setTimeSend(LocalDateTime.now());
        message.setIsRead(messageRequest.getIsRead());

        Chat chat = Chat.builder()
                .userCreator(userSenderRef)
                .userReceiver(userReceiverRef)
                .build();

        message.setChat(chat);
        message.setLastChatMessage(chat);
        chat.setLastMessage(message);
        messageRepository.save(message);

        ChatResponse chatResponseReceiver = ChatResponse.builder()
                .chatId(chat.getId())
                .senderUserId(userSenderRef.getId())
                .receiverUserId(userReceiverRef.getId())
                .email(userReceiverRef.getEmail())
                .username(userReceiverRef.getUsername())
                .avatarURL(Optional.ofNullable(userReceiverRef.getAvatar()).map(Avatar::getAvatarURL).orElse(null))
                .lastMessageId(message.getId())
                .lastMessage(message.getContent())
                .lastMessageTimeSend(message.getTimeSend())
                .lastMessageIsRead(message.getIsRead())
                .numberUnreadMessages(0L)
                .build();


        if (userSenderRef.getId().equals(userReceiverRef.getId())) {
            return Map.of(userSenderRef.getId(), chatResponseReceiver);
        }

        ChatResponse chatResponseSender = ChatResponse.builder()
                .chatId(chat.getId())
                .senderUserId(userReceiverRef.getId())
                .receiverUserId(userSenderRef.getId())
                .email(userSenderRef.getEmail())
                .username(userSenderRef.getUsername())
                .avatarURL(Optional.ofNullable(userSenderRef.getAvatar()).map(Avatar::getAvatarURL).orElse(null))
                .lastMessageId(message.getId())
                .lastMessage(message.getContent())
                .lastMessageTimeSend(message.getTimeSend())
                .lastMessageIsRead(message.getIsRead())
                .numberUnreadMessages(1L)
                .build();

        return Map.of(userSenderRef.getId(), chatResponseReceiver, userReceiverRef.getId(), chatResponseSender);
    }

    @Override
    @Transactional
    public MessageResponse removeMessage(MessageRequest messageRequest) {
        Message message = messageMapper.messageRequestToMessage(messageRequest);
        messageRepository.delete(message);

        return messageMapper.messageToMessageResponse(message);
    }


}
