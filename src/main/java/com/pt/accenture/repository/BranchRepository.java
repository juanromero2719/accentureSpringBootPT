package com.pt.accenture.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.pt.accenture.domain.Branch;

@Repository
public class BranchRepository {

	private static final RowMapper<Branch> BRANCH_MAPPER = BranchRepository::mapRow;

	private final NamedParameterJdbcTemplate jdbcTemplate;

	public BranchRepository(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Branch create(UUID franchiseId, String name) {
		String sql = "INSERT INTO branch (franchise_id, name) VALUES (:franchiseId, :name) RETURNING id, franchise_id, name";
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("franchiseId", franchiseId)
				.addValue("name", name);
		return jdbcTemplate.queryForObject(sql, params, BRANCH_MAPPER);
	}

	public boolean existsByFranchiseId(UUID franchiseId) {
		String sql = "SELECT EXISTS(SELECT 1 FROM franchise WHERE id = :franchiseId)";
		MapSqlParameterSource params = new MapSqlParameterSource("franchiseId", franchiseId);
		return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, params, Boolean.class));
	}

	public Branch findByIdAndFranchiseId(UUID branchId, UUID franchiseId) {
		String sql = "SELECT id, franchise_id, name FROM branch WHERE id = :branchId AND franchise_id = :franchiseId";
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("branchId", branchId)
				.addValue("franchiseId", franchiseId);
		try {
			return jdbcTemplate.queryForObject(sql, params, BRANCH_MAPPER);
		} catch (org.springframework.dao.EmptyResultDataAccessException ex) {
			return null;
		}
	}

	public Branch updateName(UUID branchId, UUID franchiseId, String name) {
		String sql = "UPDATE branch SET name = :name WHERE id = :branchId AND franchise_id = :franchiseId RETURNING id, franchise_id, name";
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("branchId", branchId)
				.addValue("franchiseId", franchiseId)
				.addValue("name", name);
		try {
			return jdbcTemplate.queryForObject(sql, params, BRANCH_MAPPER);
		} catch (org.springframework.dao.EmptyResultDataAccessException ex) {
			return null;
		}
	}

	private static Branch mapRow(ResultSet rs, int rowNum) throws SQLException {
		UUID id = rs.getObject("id", UUID.class);
		UUID franchiseId = rs.getObject("franchise_id", UUID.class);
		String name = rs.getString("name");
		return new Branch(id, franchiseId, name);
	}
}

