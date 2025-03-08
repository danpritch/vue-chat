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

	private final Sinks.Many<User> userSink = Sinks.many().multicast().onBackpressureBuffer();

	public Flux<User> streamUsers() {
		return Flux.concat(userStore.listUsers(), userSink.asFlux());
	}

	public Flux<String> streamUsersAsJson() {
		return streamUsers().flatMap(user -> user.toJson());
	}

	public Mono<User> createUser(User user) {
		return userStore.createUser(user);
	}

	public void emitUser(User user) {
		userSink.tryEmitNext(user);
	}

}
