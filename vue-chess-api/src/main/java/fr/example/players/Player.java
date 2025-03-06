package fr.example.players;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Data
public class Player {

	private final Long id;
	private final String name;

	/*
	 * Formatting the JSON like this is not ideal, but it avoids a dependency on ObjectMapper,
	 * and is easily testable.
	 */
	public Mono<String> toJson() {
		try {
			String json = String.format("{\"id\":%d,\"name\":\"%s\"}", id, name);
			return Mono.just(json);
		} catch (Exception e) {
			log.error("Error converting {} to JSON: {}", toString(), e.getMessage());
			return Mono.empty();
		}
	}
}
