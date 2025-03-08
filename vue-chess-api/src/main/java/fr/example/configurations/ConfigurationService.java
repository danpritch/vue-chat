package fr.example.configurations;

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
	
	@PostConstruct
	public void postConstruct() {
		
		if (!isConfigured("debezium")) {
			log.info("Setting up Debezium");
			Optional<String> connectorJson = configurationData.get("data/connector.json");
			if (connectorJson.isPresent()) {
				boolean success = configurationSender.post("http://localhost:8083/connectors",connectorJson.get());
				if (success) {
					log.info("Finished setting up Debezium");
					setConfigured("debezium");
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
