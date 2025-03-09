package fr.example.conversations.adapters;

import java.util.Arrays;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.SelectConditionStep;
import org.jooq.SelectSelectStep;
import org.springframework.stereotype.Repository;

import fr.example.conversations.Conversation;
import fr.example.conversations.ConversationStore;
import fr.example.database.Routines;
import fr.example.database.tables.OwnerConversations;
import fr.example.database.tables.records.OwnerConversationsRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PostgresConversationStore implements ConversationStore {
	
	private final DSLContext reactiveDslContext;
	
	@Override
	public Flux<Conversation> listUserConversations(Long userId) {
		SelectConditionStep<OwnerConversationsRecord> query = reactiveDslContext.selectFrom(OwnerConversations.OWNER_CONVERSATIONS)
				.where(OwnerConversations.OWNER_CONVERSATIONS.PARTICIPANT_IDS.contains(new Long[] {userId}));
		return Flux.from(query).map(u -> new Conversation(u.getConversationId(), u.getOwnerId(), Arrays.stream(u.getParticipantIds()).toList()));
	}

	@Override
	public Mono<Conversation> createConversation(Conversation conversation) {
		Long[] parts = conversation.getParticipantIds().toArray(new Long[0]);
		SelectSelectStep<Record1<Long>> query = reactiveDslContext.select(Routines.createConversation(conversation.getOwnerId(), parts));
		return Mono.from(query).map(q -> new Conversation(q.component1(), conversation.getOwnerId(), conversation.getParticipantIds()));
	}

}
