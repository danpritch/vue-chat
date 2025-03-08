package fr.example.users;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserStore {
	
	Flux<User> listUsers();
	
	Mono<User> createUser(User user);

}
