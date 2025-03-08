package fr.example.conversations.adapters;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.SelectSelectStep;
import org.springframework.stereotype.Repository;

import fr.example.conversations.Conversation;
import fr.example.conversations.ConversationStore;
import fr.example.database.Routines;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class PostgresConversationStore implements ConversationStore {
	
	private final DSLContext reactiveDslContext;

	@Override
	public Mono<Conversation> createConversation(Conversation conversation) {
		Long[] parts = conversation.getParticpantIds().toArray(new Long[0]);
		SelectSelectStep<Record1<Long>> query = reactiveDslContext.select(Routines.createConversation(conversation.getOwnerId(), parts));
		return Mono.from(query).map(q -> new Conversation(q.component1(), conversation.getOwnerId(), conversation.getParticpantIds()));
	}
	

}
