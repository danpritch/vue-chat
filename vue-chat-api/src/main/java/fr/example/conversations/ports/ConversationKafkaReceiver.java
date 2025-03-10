package fr.example.conversations.ports;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import fr.example.conversations.Conversation;
import fr.example.conversations.ConversationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class ConversationKafkaReceiver {
	
	private final ConversationService conversationService;

	@KafkaListener(topics = "kafka.chat.conversation_owner_participants_table", containerFactory = "conversationKafkaListenerContainerFactory")
	public void listen(@Header(KafkaHeaders.RECEIVED_KEY) String key, Conversation conversation) {
		log.info("Received message with key: " + key + " and value: " + conversation.toString());
		conversationService.emitConversation(conversation);
	}

}