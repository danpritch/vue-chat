package fr.example.conversations;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class ConversationService {
	
	private final ConversationStore conversationStore;

	public Mono<Conversation> createConversation(Long ownerId, Conversation conversation) {
		conversation.setOwnerId(ownerId);
		conversation.getParticpantIds().add(ownerId);
		return conversationStore.createConversation(conversation);
	}

}
