package com.moro.tchat.repository;

import com.moro.tchat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("""
            select c from Chat c
            where (c.userCreator.id = :userId
            and (upper(c.userReceiver.username) like concat(upper(:identifier), '%')
            or upper(c.userReceiver.email) like concat(upper(:identifier), '%')))
            or (c.userReceiver.id = :userId
            and (upper(c.userCreator.username) like concat(upper(:identifier), '%')
            or upper(c.userCreator.email) like concat(upper(:identifier), '%')))
            group by c.id, c.lastMessage.timeSend
            order by c.lastMessage.timeSend desc
            """)
    List<Chat> findByUserIdentifier(@Param("identifier") String identifier, @Param("userId") Long userId);

    @Query(value = """
            SELECT user_creator_id FROM chat
            WHERE user_receiver_id = :userId
            UNION
            SELECT user_receiver_id FROM chat
            WHERE user_creator_id = :userId
            """, nativeQuery = true)
    List<Long> findNotCreatedYetChats(@Param("userId") Long userId);

    @Query("""
        select c.id
        from Chat c
        where c.lastMessage.userReceiver.id = :userId
        and c.lastMessage.isRead = false
            """)
    List<Long> findChatsWithUnreadMessages(@Param("userId") Long userId);

}
