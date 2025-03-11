package fr.example.messages.adapters;

import org.jooq.DSLContext;
import org.jooq.InsertResultStep;
import org.jooq.SelectConditionStep;
import org.springframework.stereotype.Repository;

import fr.example.database.tables.Messages;
import fr.example.database.tables.UserMessages;
import fr.example.database.tables.records.MessagesRecord;
import fr.example.database.tables.records.UserMessagesRecord;
import fr.example.messages.Message;
import fr.example.messages.MessageStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PostgresMessageStore implements MessageStore {
	
	private final DSLContext reactiveDslContext;

	@Override
	public Mono<Message> createMessage(Message message) {
		InsertResultStep<MessagesRecord> query = reactiveDslContext.insertInto(Messages.MESSAGES)
				.set(Messages.MESSAGES.CONVERSATION_ID, message.getConversationId())
				.set(Messages.MESSAGES.SENDER_ID, message.getSenderId())
				.set(Messages.MESSAGES.CONTENT, message.getContent())
				.returning();
		return Mono.from(query).map(m -> new Message(m.getId(), m.getConversationId(), m.getSenderId(), m.getContent(), null));
	}

	@Override
	public Flux<Message> listUserMessages(Long userId) {
		SelectConditionStep<UserMessagesRecord> query = reactiveDslContext.selectFrom(UserMessages.USER_MESSAGES)
				.where(UserMessages.USER_MESSAGES.PARTICIPANT_ID.eq(userId));
		return Flux.from(query).map(m -> new Message(m.getId(), m.getConversationId(), m.getSenderId(), m.getContent(), m.getParticipantId()));
	}

}
