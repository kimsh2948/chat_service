package com.example.chatservice.config;

import com.example.chatservice.domain.ChatMessage;
import com.example.chatservice.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ChatHandler {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public Mono<Void> handle(WebSocketSession webSocketSession) {
        return webSocketSession.send()
    }

}
