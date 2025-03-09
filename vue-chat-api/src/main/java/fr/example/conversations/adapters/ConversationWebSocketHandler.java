package fr.example.conversations.adapters;

import java.net.URI;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.util.UriComponentsBuilder;

import fr.example.config.ReactiveWebSocketHandler;
import fr.example.conversations.ConversationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Component
public class ConversationWebSocketHandler implements ReactiveWebSocketHandler {

	private static final String CONVERSATIONS_PATH = "/ws/users/{ownerId}/conversations";

	private final ConversationService conversationService;

	@Override
	public String getPath() {
		return CONVERSATIONS_PATH;
	}

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        URI uri = session.getHandshakeInfo().getUri();
        List<String> segments = UriComponentsBuilder.fromUri(uri)
                                      .build()
                                      .getPathSegments();
        Long ownerId = Long.parseLong(segments.get(2));
        return session.send(conversationService.streamConversationsAsJson(ownerId).doOnNext(u -> log.info("Sending message: {}", u)).map(session::textMessage));
    }

}
