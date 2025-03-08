package fr.example.configurations.adapters;

import java.util.Optional;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import fr.example.configurations.Configuration;
import fr.example.configurations.ConfigurationStore;
import fr.example.database.tables.Configurations;
import fr.example.database.tables.records.ConfigurationsRecord;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class PostgresConfigurationStore implements ConfigurationStore {
	
	private final DSLContext regularDslContext;

    public Optional<Configuration> findConfigurationByName(String name) {
		return regularDslContext.selectFrom(Configurations.CONFIGURATIONS)
				.where(Configurations.CONFIGURATIONS.NAME.eq(name))
				.fetchOptional().map(r -> new Configuration(r.getId(), r.getName(), r.getConfigured()));
	}
	
	public Configuration createConfiguration(Configuration configuration) {
		ConfigurationsRecord r = regularDslContext.insertInto(Configurations.CONFIGURATIONS)
			.set(Configurations.CONFIGURATIONS.NAME, configuration.getName())
			.set(Configurations.CONFIGURATIONS.CONFIGURED, configuration.getConfigured())
			.onConflictDoNothing().returning().fetchOne();
		return new Configuration(r.getId(), r.getName(), r.getConfigured());
	}
	
}
