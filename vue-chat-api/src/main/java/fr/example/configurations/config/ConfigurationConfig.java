package fr.example.configurations.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "vue-chat-api.configuration")
public class ConfigurationConfig {
	
	private String debeziumUrl;
	private String ksqldbUrl;

}