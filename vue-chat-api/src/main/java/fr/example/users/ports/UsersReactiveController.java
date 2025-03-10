package fr.example.users.ports;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.example.users.User;
import fr.example.users.UserService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "http://localhost:5173/")
@RequiredArgsConstructor
@RestController
public class UsersReactiveController {
	
    private final UserService userService;

    @PostMapping("/users")
    public Mono<User> createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

}
