package com.moro.tchat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {

    private Long chatId;

    private Long senderUserId;

    private Long receiverUserId;

    private String email;

    private String username;

    private String avatarURL;

    private Long lastMessageId;

    private String lastMessage;

    private LocalDateTime lastMessageTimeSend;

    private Boolean lastMessageIsRead;

    private Long numberUnreadMessages;

}
