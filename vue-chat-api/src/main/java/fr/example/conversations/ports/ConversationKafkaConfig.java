package fr.example.conversations.ports;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import fr.example.config.KafkaConfig;
import fr.example.conversations.Conversation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class ConversationKafkaConfig {
	
	private final KafkaConfig kafkaConfig;

	@Bean
	ConsumerFactory<String, Conversation> conversationConsumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getKafkaHost());
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "vue-chat-api");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(Conversation.class));
	}

	@Bean
	ConcurrentKafkaListenerContainerFactory<String, Conversation> conversationKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Conversation> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(conversationConsumerFactory());
		return factory;
	}

}
