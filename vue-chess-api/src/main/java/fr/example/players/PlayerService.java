package fr.example.players;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@RequiredArgsConstructor
@Service
public class PlayerService {
	
	private final PlayerStore playerStore;
	
    private final Sinks.Many<Player> playerSink = Sinks.many().multicast().onBackpressureBuffer();
	
    public Flux<Player> streamPlayers() {
        return Flux.concat(playerStore.listPlayers(), playerSink.asFlux());
    }
    
    public Flux<String> streamPlayersAsJson() {
        return streamPlayers().flatMap(player -> player.toJson());
    }
	
	public Mono<Player> createPlayer(Player player) {
		return playerStore.createPlayer(player);
	}

}
