package com.example.chatservice.config;

import com.example.chatservice.domain.ChatMessage;
import com.example.chatservice.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ChatHandler implements WebSocketHandler {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
        Flux<ChatMessage> chatMessageFlux = this.chatMessageRepository.findWithTailableCursorBy();

        Flux<String> messageFlux = webSocketSession.receive()
                .map(WebSocketMessage::getPayloadAsText);

        return webSocketSession.send(
                chatMessageFlux.mergeWith(messageFlux).map(webSocketSession::textMessage);
        );
    }

}
