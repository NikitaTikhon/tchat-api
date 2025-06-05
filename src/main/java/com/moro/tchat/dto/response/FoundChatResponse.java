package com.moro.tchat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoundChatResponse {

    private Long receiverUserId;

    private String username;

    private String avatarURL;

}
