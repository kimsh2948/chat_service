package com.example.chatservice.config;

import com.example.chatservice.domain.ChatMessage;
import com.example.chatservice.repository.ChatMessageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatHandler implements WebSocketHandler {

    private final ChatMessageRepository chatMessageRepository;

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
        log.info("open-chat");
        Flux<ChatMessage> chatMessageFlux = this.chatMessageRepository.findWithTailableCursorBy();

        Flux<String> messageFlux = webSocketSession.receive()
                .map(WebSocketMessage::getPayloadAsText);

        Flux<ChatMessage> newChatMessageFlux = messageFlux
                .map(message -> {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setMessage(message);
                    chatMessage.setCreatedAt(LocalDateTime.now());
                    System.out.println(chatMessage.getMessage());
                    return chatMessage;
                })
                .flatMap(chatMessageRepository::save);

        ObjectMapper objectMapper = new ObjectMapper();
        Flux<String> chatMessageStringFlux = chatMessageFlux.mergeWith(newChatMessageFlux)
                .map(chatMessage -> {
                    try {
                        return objectMapper.registerModule(new JavaTimeModule()).writeValueAsString(chatMessage);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Failed to convert ChatMessage to JSON string", e);
                    }
                });

        return webSocketSession.send(
                chatMessageStringFlux.map(webSocketSession::textMessage).log()
        );
    }

}
