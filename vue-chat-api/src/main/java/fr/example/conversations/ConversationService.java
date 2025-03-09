package fr.example.conversations;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@RequiredArgsConstructor
@Service
public class ConversationService {
	
	private final ConversationStore conversationStore;
	
	private final Sinks.Many<Conversation> conversationSink = Sinks.many().multicast().onBackpressureBuffer();

	public Flux<Conversation> streamConversations(Long ownerId) {
		Flux<Conversation> sinkFlux = conversationSink.asFlux().filter(conv -> conv.getOwnerId().equals(ownerId));
		Flux<Conversation> storeFlux = conversationStore.listOwnerConversations(ownerId);
		return Flux.merge(storeFlux, sinkFlux);
	}
	
	public Flux<String> streamConversationsAsJson(Long ownerId) {
		return streamConversations(ownerId).flatMap(conv -> conv.toJson());
	}

	public Mono<Conversation> createConversation(Long ownerId, Conversation conversation) {
		conversation.setOwnerId(ownerId);
		conversation.getParticipantIds().add(ownerId);
		return conversationStore.createConversation(conversation);
	}
	
	public void emitConversation(Conversation conversation) {
		conversationSink.tryEmitNext(conversation);
	}

}
