package fr.example.messages;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageService {
	
	private final MessageStore messageStore;
	
	private final Sinks.Many<Message> messageSink = Sinks.many().multicast().onBackpressureBuffer();

	public Flux<Message> streamMessages(Long userId) {
		Flux<Message> sinkFlux = messageSink.asFlux();
		Flux<Message> storeFlux = messageStore.listUserMessages(userId);
		return Flux.merge(storeFlux, sinkFlux);
	}
	
	public Flux<String> streamMessagesAsJson(Long userId) {
		return streamMessages(userId).flatMap(m -> m.toJson());
	}

	public Mono<Message> createMessage(Long senderId, Message message) {
		message.setSenderId(senderId);
		return messageStore.createMessage(message);
	}
	
	public void emitMessage(Message message) {
	    Sinks.EmitResult result = messageSink.tryEmitNext(message);
	    if(result != Sinks.EmitResult.OK) {
	        log.warn("Message emission failed with result: {}", result);
	    }
	}

}
