package fr.example.users.ports;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import fr.example.users.User;
import fr.example.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class KafkaReceiver {
	
	private final UserService userService;

	@KafkaListener(topics = "postgres.chat.users", containerFactory = "userKafkaListenerContainerFactory")
	public void listen(@Header(KafkaHeaders.RECEIVED_KEY) String key, User user) {
		log.info("Received message with key: " + key + " and value: " + user.toString());
		userService.emitUser(user);
	}

}