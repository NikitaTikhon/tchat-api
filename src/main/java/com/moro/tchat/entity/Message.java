package com.moro.tchat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Message")
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String content;

    private Boolean isRead;

    private LocalDateTime timeSend;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Chat chat;

    @OneToOne(mappedBy = "lastMessage")
    private Chat lastChatMessage;

    @ManyToOne
    private User userSender;

    @ManyToOne
    private User userReceiver;

}
