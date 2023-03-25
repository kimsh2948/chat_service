package com.example.chatservice.config;

import com.example.chatservice.domain.ChatMessage;
import com.example.chatservice.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatHandler implements WebSocketHandler {

    private final ChatMessageRepository chatMessageRepository;

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
        System.out.println("open");
        Flux<ChatMessage> chatMessageFlux = this.chatMessageRepository.findWithTailableCursorBy();

        Flux<String> messageFlux = webSocketSession.receive()
                .map(WebSocketMessage::getPayloadAsText);

        return webSocketSession.send(
                chatMessageFlux.merge(messageFlux).map(webSocketSession::textMessage)
        );
    }

}
