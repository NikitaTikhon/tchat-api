package com.moro.tchat.repository;

import com.moro.tchat.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByChatIdOrderByTimeSendAsc(Long chatId);

    List<Message> getAllByIdIn(List<Long> ids);

}
