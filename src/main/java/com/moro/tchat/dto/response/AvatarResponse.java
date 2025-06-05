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
public class AvatarResponse {

    private Long id;

    private Long userId;

    private String publicId;

    private String avatarURL;

    private LocalDateTime creationDate;

}
