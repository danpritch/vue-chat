package fr.example.messages;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MessageStore {

	Mono<Message> createMessage(Message message);

	Flux<Message> listUserMessages(Long userId);

}
