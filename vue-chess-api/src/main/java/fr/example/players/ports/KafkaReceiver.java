package fr.example.players.ports;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import fr.example.players.Player;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KafkaReceiver {

	@KafkaListener(topics = "postgres.vue_chess.players", containerFactory = "playerKafkaListenerContainerFactory")
	public void listen(@Header(KafkaHeaders.RECEIVED_KEY) String key, Player player) {
		log.info("Received message with key: " + key + " and value: " + player.toString());
	}

}