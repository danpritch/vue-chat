package fr.example.configurations.adapters;

import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import fr.example.configurations.ConfigurationSender;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RestTemplateSender implements ConfigurationSender {
	
	@Override
	public boolean post(String url, String body) {
	    RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	    HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
	    
	    try {
	        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
	        return response.getStatusCode().is2xxSuccessful();
	    } catch (RestClientException e) {
	        log.error("Error occurred during POST request: " + e.getMessage());
	        return false;
	    }
	}

}
