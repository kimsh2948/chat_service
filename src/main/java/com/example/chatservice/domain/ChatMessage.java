package com.example.chatservice.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chat_messages")
@Getter
public class ChatMessage {

    @Id
    private String id;

    private String roomId;

    private String sender;

    private String message;

    private LocalDateTime createdAt;
}
