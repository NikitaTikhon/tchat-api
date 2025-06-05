package com.moro.tchat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "User")
@Table(name = "`user`")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(length = 20, unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "boolean default false")
    private boolean enabled;

    @OneToOne(mappedBy="user")
    private VerificationToken verificationToken;

    @OneToOne(mappedBy = "userAvatar")
    private Avatar avatar;

    @OneToMany(mappedBy = "userCreator")
    private List<Chat> chatsCreated;

    @OneToMany(mappedBy = "userReceiver")
    private List<Chat> chatsReceived;


    @OneToMany(mappedBy = "userSender")
    private List<Message> messagesSend;

    @OneToMany(mappedBy = "userReceiver")
    private List<Message> messagesReceived;

}
