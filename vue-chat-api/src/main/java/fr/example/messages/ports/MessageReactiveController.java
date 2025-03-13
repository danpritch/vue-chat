package fr.example.messages.ports;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.example.messages.Message;
import fr.example.messages.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@CrossOrigin(origins = "http://localhost:5173" )
@RequiredArgsConstructor
@RestController
public class MessageReactiveController {
	
	private final MessageService messageService;
	
    @GetMapping(value = "/users/{userId}/messages", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Message>> streamMessages(@PathVariable("userId") Long userId) {
    	return messageService.streamMessages(userId).map(m -> ServerSentEvent.builder(m).build());
    }
	
	@PostMapping("/users/{senderId}/messages")
	public Mono<Message> createMessage(@PathVariable("senderId") Long senderId, @RequestBody Message message) {
		return messageService.createMessage(senderId, message);
	}

}
