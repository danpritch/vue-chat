package fr.example.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import fr.example.players.Player;

@Configuration
public class KafkaConfig {

	@Bean
	ConsumerFactory<String, Player> playerConsumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "vue-chess-api");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(Player.class));
	}

	@Bean
	ConcurrentKafkaListenerContainerFactory<String, Player> playerKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Player> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(playerConsumerFactory());
		return factory;
	}

	@Bean
	NewTopic playersTopic() {
		return new NewTopic("postgres.vue_chess.players", 1, (short) 1);
	}

}
