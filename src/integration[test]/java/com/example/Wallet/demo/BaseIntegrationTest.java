package com.example.Wallet.demo;

import org.junit.jupiter.api.Tag;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("Integration test")
@Testcontainers
public abstract class BaseIntegrationTest {

    @LocalServerPort
    protected int port;

    static PostgreSQLContainer postgres=new PostgreSQLContainer("postgres:15-alpine")
            .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("postgres")));

    static {
        postgres.start();
    }

    @DynamicPropertySource
    static void configurePropertiees(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }
}
