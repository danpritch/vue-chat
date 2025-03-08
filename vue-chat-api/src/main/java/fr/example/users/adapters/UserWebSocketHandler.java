package fr.example.users.adapters;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;

import fr.example.config.ReactiveWebSocketHandler;
import fr.example.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Component
public class UserWebSocketHandler implements ReactiveWebSocketHandler {

	private static final String USERS_PATH = "/ws/users";

	private final UserService userService;

	@Override
	public String getPath() {
		return USERS_PATH;
	}

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.send(userService.streamUsersAsJson().doOnNext(u -> log.info("Sending message: {}", u)).map(session::textMessage));
    }

}
