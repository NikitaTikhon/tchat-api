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
public class MessageResponse {

    private Long id;

    private Long chatId;

    private Long userSenderId;

    private Long userReceiverId;

    private String content;

    private Boolean isRead;

    private LocalDateTime timeSend;

}
