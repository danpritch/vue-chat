package fr.example.configurations.adapters;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.springframework.stereotype.Component;

import fr.example.configurations.ConfigurationData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LocalConfigurationData implements ConfigurationData {
	
	@Override
	public Optional<String> get(String fileName) {
		try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
	        if (inputStream == null) {
	            throw new IllegalArgumentException("File not found: " + fileName);
	        }
	        return Optional.of(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8));
	    } catch (IOException e) {
	        log.error(e.getMessage());
	        return Optional.empty();
	    }
	}

}