package fr.example.conversations;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConversationService {
	
	private final ConversationStore conversationStore;
	
	private final Sinks.Many<Conversation> conversationSink = Sinks.many().multicast().directAllOrNothing();

	public Flux<Conversation> streamConversations(Long userId) {
		Flux<Conversation> sinkFlux = conversationSink.asFlux().filter(conv -> conv.getParticipantIds() != null && conv.getParticipantIds().contains(userId));
		Flux<Conversation> storeFlux = conversationStore.listUserConversations(userId);
		return Flux.merge(storeFlux, sinkFlux);
	}
	
	public Flux<String> streamConversationsAsJson(Long userId) {
		return streamConversations(userId).flatMap(conv -> conv.toJson());
	}

	public Mono<Conversation> createConversation(Long ownerId, Conversation conversation) {
		conversation.setOwnerId(ownerId);
		conversation.getParticipantIds().add(ownerId);
		return conversationStore.createConversation(conversation);
	}
	
	public void emitConversation(Conversation conversation) {
	    Sinks.EmitResult result = conversationSink.tryEmitNext(conversation);
	    if(result != Sinks.EmitResult.OK) {
	        log.warn("Emission failed with result: {}", result);
	    }
	}

}
