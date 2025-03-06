package fr.example.players;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerStore {
	
	Flux<Player> listPlayers();
	
	Mono<Player> createPlayer(Player player);

}
