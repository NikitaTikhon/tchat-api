package com.moro.tchat.controller;

import com.moro.tchat.config.security.UserDetailsImpl;
import com.moro.tchat.dto.request.MessageRequest;
import com.moro.tchat.dto.response.ChatResponse;
import com.moro.tchat.dto.response.MessageResponse;
import com.moro.tchat.dto.ChatMessages;
import com.moro.tchat.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class MessageController {

    private MessageService messageService;

    private SimpMessagingTemplate simpMessagingTemplate;


    @GetMapping("/v1/messages/{chatId}")
    ResponseEntity<List<MessageResponse>> getMessagesByChatId(@PathVariable("chatId") Long chatId) {
        List<MessageResponse> messageResponses = messageService.getMessagesByChatId(chatId);
        return ResponseEntity.ok(messageResponses);
    }

    @MessageMapping("/chat/messages")
    public void saveMessage(@ModelAttribute("newMessage") MessageRequest messageRequest, Principal principal) {
        MessageResponse messageResponse = messageService.saveMessage(messageRequest, principal.getName());
        simpMessagingTemplate.convertAndSend("/topic/chat/%s/messages"
                .formatted(messageRequest.getChatId()), messageResponse);
        simpMessagingTemplate.convertAndSend("/topic/chat/%s/lastMessages"
                .formatted(messageRequest.getChatId()), messageResponse);
    }

    @MessageMapping("/chat/messages/read")
    public void updateMessagesIsRead(@ModelAttribute("unreadMessages") ChatMessages chatMessages) {
        List<Long> updatedMessagesIsReadIds = messageService.updateMessagesIsRead(chatMessages);

        simpMessagingTemplate.convertAndSend("/topic/chat/%s/messages/read".formatted(chatMessages.getChatId()),
                ChatMessages.builder()
                        .chatId(chatMessages.getChatId())
                        .messageIds(updatedMessagesIsReadIds)
                        .build());
    }

    @MessageMapping("/chat/addChat")
    public void saveNewChatAndMessage(@ModelAttribute("newMessage") MessageRequest messageRequest, Authentication authentication) {
        Map<Long, ChatResponse> chatResponse = messageService.createNewChatAndSaveMessage(messageRequest, authentication.getName());

        Long authUserId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        if (authUserId.equals(messageRequest.getUserReceiverId())) {
            simpMessagingTemplate.convertAndSend("/topic/chat/%s/addChat"
                    .formatted(authUserId), chatResponse.get(authUserId));
        } else {
            simpMessagingTemplate.convertAndSend("/topic/chat/%s/addChat"
                    .formatted(authUserId), chatResponse.get(authUserId));
            simpMessagingTemplate.convertAndSend("/topic/chat/%s/addChat"
                    .formatted(messageRequest.getUserReceiverId()), chatResponse.get(messageRequest.getUserReceiverId()));
        }
    }

    @MessageMapping("/chat/messages/remove")
    public void removeMessage(@ModelAttribute("message") MessageRequest messageRequest) {
        MessageResponse messageResponse = messageService.removeMessage(messageRequest);
    }

}
