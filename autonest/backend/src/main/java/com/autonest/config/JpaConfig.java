package com.autonest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * JPA configuration — enables auditing for createdAt/updatedAt fields.
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.autonest.repository.postgres")
public class JpaConfig {
}
