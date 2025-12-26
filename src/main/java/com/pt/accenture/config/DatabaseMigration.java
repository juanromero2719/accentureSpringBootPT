package com.pt.accenture.config;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

@Component
public class DatabaseMigration implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(DatabaseMigration.class);
	private final JdbcTemplate jdbcTemplate;

	public DatabaseMigration(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void run(String... args) {
		try {
			log.info("Ejecutando migraciones de base de datos...");
			
			ClassPathResource resource = new ClassPathResource("migrations/001_init_franchises.sql");
			String sql = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
			
			String[] statements = sql.split(";");
			for (String statement : statements) {
				String trimmed = statement.trim();
				if (!trimmed.isEmpty()) {
					jdbcTemplate.execute(trimmed);
				}
			}
			
			log.info("Migraciones ejecutadas correctamente");
		} catch (Exception e) {
			log.error("Error al ejecutar migraciones", e);
			throw new RuntimeException("No se pudieron ejecutar las migraciones", e);
		}
	}
}

