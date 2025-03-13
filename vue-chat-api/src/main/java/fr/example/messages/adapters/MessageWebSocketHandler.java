package fr.example.messages.adapters;

import java.net.URI;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.util.UriComponentsBuilder;

import fr.example.config.ReactiveWebSocketHandler;
import fr.example.messages.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Component
public class MessageWebSocketHandler implements ReactiveWebSocketHandler {

	private static final String MESSAGES_PATH = "/ws/users/{ownerId}/messages";

	private final MessageService messageService;

	@Override
	public String getPath() {
		return MESSAGES_PATH;
	}

	@Override
	public Mono<Void> handle(WebSocketSession session) {
		URI uri = session.getHandshakeInfo().getUri();
		List<String> segments = UriComponentsBuilder.fromUri(uri).build().getPathSegments();
		Long ownerId = Long.parseLong(segments.get(2));
		log.info("New message websocket session for owner: {}", ownerId);
		return session.send(messageService.streamMessagesAsJson(ownerId).doOnNext(u -> log.info("Sending message: {}", u)).map(session::textMessage));
	}

}
