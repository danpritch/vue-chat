package fr.example.configurations.adapters;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import fr.example.configurations.TopicCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class KafkaTopicCreator implements TopicCreator {
	
	private final KafkaAdmin admin;

	@Override
	public boolean createTopic(List<String> names) {
		try (AdminClient adminClient = AdminClient.create(admin.getConfigurationProperties())) {
			List<NewTopic> topics = names.stream().map(n -> new NewTopic(n, 1, (short) 1)).collect(Collectors.toList());
			adminClient.createTopics(topics).all().get();
			log.info("Topic(s) created successfully!");
			return true;
		} catch (InterruptedException | ExecutionException e) {
			log.error("Failed to create topic: " + e.getMessage(), e);
			return false;
		}
	}

}