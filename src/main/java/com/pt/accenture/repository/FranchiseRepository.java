package com.pt.accenture.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.pt.accenture.domain.Franchise;

@Repository
public class FranchiseRepository {

	private static final RowMapper<Franchise> FRANCHISE_MAPPER = FranchiseRepository::mapRow;

	private final NamedParameterJdbcTemplate jdbcTemplate;

	public FranchiseRepository(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Franchise create(String name) {
		String sql = "INSERT INTO franchise (name) VALUES (:name) RETURNING id, name";
		MapSqlParameterSource params = new MapSqlParameterSource("name", name);
		return jdbcTemplate.queryForObject(sql, params, FRANCHISE_MAPPER);
	}

	public boolean existsById(UUID franchiseId) {
		String sql = "SELECT EXISTS(SELECT 1 FROM franchise WHERE id = :franchiseId)";
		MapSqlParameterSource params = new MapSqlParameterSource("franchiseId", franchiseId);
		return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, params, Boolean.class));
	}

	public Franchise updateName(UUID franchiseId, String name) {
		String sql = "UPDATE franchise SET name = :name WHERE id = :franchiseId RETURNING id, name";
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("franchiseId", franchiseId)
				.addValue("name", name);
		try {
			return jdbcTemplate.queryForObject(sql, params, FRANCHISE_MAPPER);
		} catch (org.springframework.dao.EmptyResultDataAccessException ex) {
			return null;
		}
	}

	private static Franchise mapRow(ResultSet rs, int rowNum) throws SQLException {
		UUID id = rs.getObject("id", UUID.class);
		String name = rs.getString("name");
		return new Franchise(id, name);
	}
}
