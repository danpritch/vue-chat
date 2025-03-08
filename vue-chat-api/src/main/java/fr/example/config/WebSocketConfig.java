package fr.example.config;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

@Configuration
@EnableWebFlux
public class WebSocketConfig {

	@Bean
	HandlerMapping webSocketHandlerMapping(List<ReactiveWebSocketHandler> handlers) {
		Map<String, WebSocketHandler> handlerMap = handlers.stream()
				.collect(Collectors.toMap(k -> k.getPath(), v -> v));

		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		mapping.setUrlMap(handlerMap);
		mapping.setOrder(-1);

		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.addAllowedOrigin("http://localhost:5173/");
		corsConfig.addAllowedHeader("*");
		corsConfig.addAllowedMethod("*");
		
		Map<String, CorsConfiguration> configMap = handlers.stream()
				.collect(Collectors.toMap(k -> k.getPath(), v -> corsConfig));

		mapping.setCorsConfigurations(configMap);
		return mapping;
	}

	@Bean
	WebSocketHandlerAdapter handlerAdapter() {
		return new WebSocketHandlerAdapter();
	}
}
