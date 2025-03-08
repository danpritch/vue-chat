package fr.example.players.ports;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import fr.example.players.Player;
import fr.example.players.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class KafkaReceiver {
	
	private final PlayerService playerService;

	@KafkaListener(topics = "postgres.vue_chess.players", containerFactory = "playerKafkaListenerContainerFactory")
	public void listen(@Header(KafkaHeaders.RECEIVED_KEY) String key, Player player) {
		log.info("Received message with key: " + key + " and value: " + player.toString());
		playerService.emitPlayer(player);
	}

}