package com.moro.tchat.mapper;

import com.moro.tchat.dto.request.MessageRequest;
import com.moro.tchat.dto.response.MessageResponse;
import com.moro.tchat.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    Message messageRequestToMessage(MessageRequest messageRequest);

    @Mapping(target = "chatId", source = "chat.id")
    @Mapping(target = "userSenderId", source = "userSender.id")
    @Mapping(target = "userReceiverId", source = "userReceiver.id")
    MessageResponse messageToMessageResponse(Message message);

    List<MessageResponse> messagesToMessageResponses(List<Message> messages);

}
