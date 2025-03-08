package fr.example.configurations;

import java.util.Optional;

public interface ConfigurationStore {

	Optional<Configuration> findConfigurationByName(String name);

	Configuration createConfiguration(Configuration configuration);

}