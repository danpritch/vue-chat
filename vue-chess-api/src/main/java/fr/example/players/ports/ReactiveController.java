package fr.example.players.ports;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.example.players.Player;
import fr.example.players.PlayerService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "http://localhost:5173/")
@RequiredArgsConstructor
@RestController
public class ReactiveController {
	
    private final PlayerService playerService;

    @PostMapping("/players")
    public Mono<Player> createPlayer(@RequestBody Player player) {
        return playerService.createPlayer(player);
    }

}
