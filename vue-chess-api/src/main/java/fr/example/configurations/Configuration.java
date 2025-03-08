package fr.example.configurations;

import lombok.Data;

@Data
public class Configuration {

	private final Long id;
	private final String name;
	private final Boolean configured;
	
	
}