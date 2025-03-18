package fr.example.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Data
public class Message {
	
	@JsonProperty("ID")
	private Long id;
	
	@JsonProperty("CONVERSATION_ID_DUP")
	private Long conversationId;
	
	@JsonProperty("SENDER_ID")
	private Long senderId;
	
	@JsonProperty("CONTENT")
	private String content;
	
	@JsonProperty("PARTICIPANT_ID")
	private Long participantId;
	
	public Mono<String> toJson() {
		try {
			String json = String.format("{\"id\":%d,\"conversation_id\":%d,\"sender_id\":%d,\"content\":\"%s\"}", id, conversationId, senderId, content);
			return Mono.just(json);
		} catch (Exception e) {
			log.error("Error converting {} to JSON: {}", toString(), e.getMessage());
			return Mono.empty();
		}
	}

}
