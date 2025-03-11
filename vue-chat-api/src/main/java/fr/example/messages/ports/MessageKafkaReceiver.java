package fr.example.messages.ports;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import fr.example.messages.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class MessageKafkaReceiver {
	
	@KafkaListener(topics = "MESSAGE_RECIPIENTS", containerFactory = "messageKafkaListenerContainerFactory")
	public void listen(@Header(KafkaHeaders.RECEIVED_KEY) String key, Message message) {
		log.info("Received message with key: " + message.toString());
	}

}