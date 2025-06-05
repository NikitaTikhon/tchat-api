package com.moro.tchat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {

    private Long id;

    private Long chatId;

    private Long userSenderId;

    private Long userReceiverId;

    private String content;

    private Boolean isRead;

}
