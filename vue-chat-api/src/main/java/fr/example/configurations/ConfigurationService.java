package fr.example.configurations;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConfigurationService {
	
	private final ConfigurationStore configurationStore;
	
	private final ConfigurationData configurationData;
	
	private final ConfigurationSender configurationSender;
	
	private final TopicCreator topicCreator;
	
	@PostConstruct
	public void postConstruct() {
		
		if(!isConfigured("kafka")) {
			log.info("Setting up Kafka");
			List<String> topics = List.of("postgres.chat.users", "postgres.chat.conversations", "postgres.chat.participants");
			if (!topics.isEmpty()) {
				boolean success = topicCreator.createTopic(topics);
				if (success) {
					log.info("Finished setting up Kafka");
					setConfigured("kafka");
				}
			}
		}
		
		if (!isConfigured("debezium")) {
			log.info("Setting up Debezium");
			Optional<String> connectorJson = configurationData.get("data/connector.json");
			if (connectorJson.isPresent()) {
				boolean success = configurationSender.post("http://localhost:8083/connectors", connectorJson.get());
				if (success) {
					log.info("Finished setting up Debezium");
					setConfigured("debezium");
				}
			}
		}
		
		if (!isConfigured("ksqldb")) {
			log.info("Setting up KSQL DB");
			Optional<String> ksqldb = configurationData.get("data/ksqldb.json");
			if (ksqldb.isPresent()) {
				boolean success = configurationSender.post("http://localhost:8088/ksql", ksqldb.get());
				if (success) {
					log.info("Finished setting up KSQL DB");
					setConfigured("ksqldb");
				}
			}	
		}
		
	}
	
	public boolean isConfigured(String name) {
		Optional<Configuration> configuration = configurationStore.findConfigurationByName(name);
		return configuration.isPresent() && configuration.get().getConfigured();
	}
	
	public void setConfigured(String name) {
		configurationStore.createConfiguration(new Configuration(null, name, true));
	}

}
