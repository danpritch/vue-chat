package fr.example.players;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerStore playerStore;

    @InjectMocks
    private PlayerService playerService;

    @Test
    void testStreamPlayers() {
        Player player1 = new Player(1L, "Alice");
        Player player2 = new Player(2L, "Bob");
        when(playerStore.listPlayers()).thenReturn(Flux.just(player1, player2));
        StepVerifier.create(playerService.streamPlayers())
                .expectNext(player1)
                .expectNext(player2)
                .thenCancel()
                .verify();
    }

    @Test
    void testStreamPlayersAsJson() {
        Player player1 = new Player(1L, "Alice");
        Player player2 = new Player(2L, "Bob");
        when(playerStore.listPlayers()).thenReturn(Flux.just(player1, player2));
        StepVerifier.create(playerService.streamPlayersAsJson())
                .expectNext("{\"id\":1,\"name\":\"Alice\"}")
                .expectNext("{\"id\":2,\"name\":\"Bob\"}")
                .thenCancel()
                .verify();
    }

    @Test
    void testCreatePlayer() {
        Player newPlayer = new Player(3L, "Charlie");
        when(playerStore.createPlayer(any(Player.class))).thenReturn(Mono.just(newPlayer));
        StepVerifier.create(playerService.createPlayer(newPlayer))
                .expectNextMatches(player -> player.getId().equals(3L) && player.getName().equals("Charlie"))
                .verifyComplete();
    }

    @Test
    void testPlayerToJsonSuccess() {
        Player player = new Player(1L, "Alice");
        StepVerifier.create(player.toJson())
                .expectNext("{\"id\":1,\"name\":\"Alice\"}")
                .verifyComplete();
    }

    @Test
    void testPlayerToJsonException() {
        Player invalidPlayer = new Player(null, null); 
        StepVerifier.create(invalidPlayer.toJson())
                .expectNext("{\"id\":null,\"name\":\"null\"}") 
                .verifyComplete();
    }
}
