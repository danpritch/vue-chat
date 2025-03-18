package fr.example.users;

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
class UserServiceTest {

    @Mock
    private UserStore userStore;

    @InjectMocks
    private UserService userService;

    @Test
    void testStreamUsers() {
        User user1 = new User(1L, "Alice");
        User user2 = new User(2L, "Bob");
        when(userStore.listUsers()).thenReturn(Flux.just(user1, user2));
        StepVerifier.create(userService.streamUsers())
                .expectNext(user1)
                .expectNext(user2)
                .thenCancel()
                .verify();
    }

    @Test
    void testCreateUser() {
        User newUser = new User(3L, "Charlie");
        when(userStore.createUser(any(User.class))).thenReturn(Mono.just(newUser));
        StepVerifier.create(userService.createUser(newUser))
                .expectNextMatches(user -> user.getId().equals(3L) && user.getName().equals("Charlie"))
                .verifyComplete();
    }

    @Test
    void testUserToJsonSuccess() {
        User user = new User(1L, "Alice");
        StepVerifier.create(user.toJson())
                .expectNext("{\"id\":1,\"name\":\"Alice\"}")
                .verifyComplete();
    }

    @Test
    void testUserToJsonException() {
        User invalidUser = new User(null, null); 
        StepVerifier.create(invalidUser.toJson())
                .expectNext("{\"id\":null,\"name\":\"null\"}") 
                .verifyComplete();
    }
}
