package fr.example.users;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserStore userStore;

	private final Sinks.Many<User> userSink = Sinks.many().multicast().directAllOrNothing();

	public Flux<User> streamUsers() {
		return Flux.merge(userStore.listUsers(), userSink.asFlux());
	}

	public Mono<User> createUser(User user) {
		return userStore.createUser(user);
	}

	public void emitUser(User user) {
		userSink.tryEmitNext(user);
	}

}
