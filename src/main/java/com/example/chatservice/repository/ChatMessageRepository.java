package com.example.chatservice.repository;

import com.example.chatservice.domain.ChatMessage;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface ChatMessageRepository extends ReactiveMongoRepository<ChatMessage, String> {

    @Tailable
    Flux<ChatMessage> findWithTailableCursorBy();
}
