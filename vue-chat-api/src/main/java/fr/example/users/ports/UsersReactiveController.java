package fr.example.users.ports;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.example.users.User;
import fr.example.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@RestController
public class UsersReactiveController {
	
    private final UserService userService;
    
    @GetMapping(value = "/users", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<User>> streamUsers() {
        Flux<ServerSentEvent<User>> userEvents = userService.streamUsers().doOnNext(u -> log.info("Sending user: {}", u))
            .map(user -> ServerSentEvent.builder(user)
                    .retry(Duration.ofSeconds(20)).build());
        return userEvents;
    }

    @PostMapping("/users")
    public Mono<User> createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

}
