package com.phincon.backend.bootcamp.marketplace.order_service.config;

import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@EnableR2dbcRepositories
@Configuration
@Slf4j
public class R2dbcConfig {
    @Bean
    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        log.debug("Connection factory set: {}", connectionFactory);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();

        populator.addScripts(
                new ClassPathResource("create_table.sql"));

        initializer.setDatabasePopulator(populator);
        return initializer;
    }
}
