package fr.example.conversations;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Data
public class Conversation {
	
	@JsonProperty("ID")
	private Long id;
	
	@JsonProperty("OWNER_ID")
	private Long ownerId;
	
	@JsonProperty("PARTICIPANT_IDS")
	private List<Long> particpantIds;

}
