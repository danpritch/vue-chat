package fr.example.conversations.ports;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.example.conversations.Conversation;
import fr.example.conversations.ConversationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@RestController
public class ConversationReactiveController {
	
	private final ConversationService conversationService;
	
//    @GetMapping(value = "/users/{userId}/conversations", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<ServerSentEvent<Conversation>> streamConversations(@PathVariable("userId") Long userId) {
//    	Flux<ServerSentEvent<Conversation>> conversationEvents = conversationService.streamConversations(userId)
//    			.map(conv -> ServerSentEvent.builder(conv).retry(Duration.ofSeconds(20)).build());
//    	return conversationEvents;
//    }
    
    @GetMapping(value = "/users/{userId}/conversations", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Conversation>> streamConversations(@PathVariable("userId") Long userId) {
    	Flux<ServerSentEvent<Conversation>> conversationEvents = conversationService.streamConversations(userId).doOnNext(u -> log.info("Sending conversation: {}", u))
    			.map(conv -> ServerSentEvent.builder(conv).build());
    	return conversationEvents;
    }   
    
    @PostMapping("/users/{ownerId}/conversations")
    public Mono<Conversation> createConversation(@PathVariable("ownerId") Long ownerId, @RequestBody Conversation conversation) {
        return conversationService.createConversation(ownerId, conversation);
    }

}
