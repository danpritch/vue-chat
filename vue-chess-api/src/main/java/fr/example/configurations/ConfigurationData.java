package fr.example.configurations;

import java.util.Optional;

public interface ConfigurationData {

	Optional<String> get(String fileName);

}