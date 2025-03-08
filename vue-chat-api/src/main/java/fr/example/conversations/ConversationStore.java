package fr.example.conversations;

import reactor.core.publisher.Mono;

public interface ConversationStore {
	
	Mono<Conversation> createConversation(Conversation conversation);

}
