package fr.example.config;

import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.connection.TransactionAwareConnectionFactoryProxy;

import io.r2dbc.spi.ConnectionFactory;
import lombok.Setter;

@Setter
@ConfigurationProperties("spring.datasource")
@Configuration
public class JooqConfig {
	
	private String url;
	private String username;
	private String password;
	private String driverClassName;

	@Bean
	DSLContext reactiveDslContext(ConnectionFactory connectionFactory) {
		return DSL.using(new TransactionAwareConnectionFactoryProxy(connectionFactory), SQLDialect.POSTGRES);
	}
	
    @Bean
    DataSource dataSource() {
        return DataSourceBuilder.create()        
        		.username(username)
                .password(password)
                .url(url)
                .driverClassName(driverClassName)
                .build();
    }
	
	@Bean
	DSLContext regularDslContext(DataSource dataSource) {
		return DSL.using(dataSource, SQLDialect.POSTGRES);
	}
}