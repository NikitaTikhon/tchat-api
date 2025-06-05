package com.moro.tchat.controller;

import com.moro.tchat.config.security.UserDetailsImpl;
import com.moro.tchat.dto.response.ChatResponse;
import com.moro.tchat.dto.response.FoundChatResponse;
import com.moro.tchat.service.ChatService;
import com.moro.tchat.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ChatController {

    private ChatService chatService;

    private UserService userService;

    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/v1/chats")
    public ResponseEntity<List<ChatResponse>> defaultGetChatsByUserIdentifier(@AuthenticationPrincipal UserDetailsImpl authUser) {
        List<ChatResponse> chatsAuthUser = chatService.findChatsByUserIdentifier("", authUser.getId());
        return ResponseEntity.ok(chatsAuthUser);
    }

    @GetMapping("/v1/chats/{identifier}")
    public ResponseEntity<List<ChatResponse>> getChatsByUserIdentifier(@PathVariable("identifier") String identifier, @AuthenticationPrincipal UserDetailsImpl authUser) {
        List<ChatResponse> chatsAuthUser = chatService.findChatsByUserIdentifier(identifier, authUser.getId());
        return ResponseEntity.ok(chatsAuthUser);
    }

    @GetMapping("/v1/chats/unread")
    public ResponseEntity<List<Long>> getChatsWithUnreadMessages(@AuthenticationPrincipal UserDetailsImpl authUser) {
        List<Long> unreadChatsIds = chatService.findChatsWithUnreadMessages(authUser.getId());
        return ResponseEntity.ok(unreadChatsIds);
    }

    @GetMapping("/v1/chats/{identifier}/new")
    public ResponseEntity<List<FoundChatResponse>> searchNewChatsByUserIdentifier(@PathVariable("identifier") String identifier, @AuthenticationPrincipal UserDetailsImpl authUser) {
        List<FoundChatResponse> foundChatResponses = userService.searchNewChatsByUserIdentifier(identifier, authUser.getId());
        return ResponseEntity.ok(foundChatResponses);
    }

    @MessageMapping("/chat/{chatId}/removeChat")
    public void removeChat(@DestinationVariable("chatId") Long chatId) {
        ChatResponse chatResponse = chatService.removeChat(chatId);

        if (chatResponse.getSenderUserId().equals(chatResponse.getReceiverUserId())) {
            simpMessagingTemplate.convertAndSend("/topic/chat/%s/removeChat"
                    .formatted(chatResponse.getSenderUserId()), chatResponse);
        } else {
            simpMessagingTemplate.convertAndSend("/topic/chat/%s/removeChat"
                    .formatted(chatResponse.getSenderUserId()), chatResponse);
            simpMessagingTemplate.convertAndSend("/topic/chat/%s/removeChat"
                    .formatted(chatResponse.getReceiverUserId()), chatResponse);
        }
    }

}
