package fr.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "vue-chat-api.configuration")
public class KafkaConfig {
	
	private String kafkaHost;

}
