package fr.example.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Data
public class User {

	private Long id;
	private String name;

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
