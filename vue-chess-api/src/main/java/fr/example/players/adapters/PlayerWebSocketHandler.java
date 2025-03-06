package fr.example.players.adapters;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;

import fr.example.config.ReactiveWebSocketHandler;
import fr.example.players.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Component
public class PlayerWebSocketHandler implements ReactiveWebSocketHandler {

	private static final String PLAYERS_PATH = "/ws/players";

	private final PlayerService playerService;

	@Override
	public String getPath() {
		return PLAYERS_PATH;
	}

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.send(playerService.streamPlayersAsJson().map(session::textMessage));
    }

}
