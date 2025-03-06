package fr.example.players.adapters;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import fr.example.players.Player;
import fr.example.players.PlayerStore;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class InMemoryPlayerStore implements PlayerStore {
	
    private final ConcurrentHashMap<Long, Player> playerMap = new ConcurrentHashMap<>();
    
	private final AtomicLong nextPlayerId = new AtomicLong(1);

	@Override
	public Flux<Player> listPlayers() {
		return Flux.fromIterable(playerMap.values());
	}

	@Override
	public Mono<Player> createPlayer(Player player) {
		Long playerId = nextPlayerId.getAndIncrement();
		Player playerWithId = new Player(playerId, player.getName());
		playerMap.put(playerId, playerWithId);
		return Mono.just(playerWithId);
	}

}
