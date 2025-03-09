package fr.example.conversations;

import java.util.List;
import java.util.stream.Collectors;

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
public class Conversation {

	@JsonProperty("CONVERSATION_ID_DUP")
	private Long id;

	@JsonProperty("OWNER_ID")
	private Long ownerId;

	@JsonProperty("PARTICIPANT_IDS")
	private List<Long> participantIds;

	public Mono<String> toJson() {
		try {
			String participantIdsStr = (participantIds != null) ? participantIds.stream().map(String::valueOf).collect(Collectors.joining(",")) : "";
			String json = String.format("{\"id\":%d,\"owner_id\":%d,\"participant_ids\":[%s]}", id, ownerId, participantIdsStr);
			return Mono.just(json);
		} catch (Exception e) {
			log.error("Error converting {} to JSON: {}", toString(), e.getMessage());
			return Mono.empty();
		}
	}

}
