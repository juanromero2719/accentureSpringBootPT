package com.pt.accenture.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.pt.accenture.api.dto.ProductWithBranchResponse;
import com.pt.accenture.domain.Product;

@Repository
public class ProductRepository {

	private static final RowMapper<Product> PRODUCT_MAPPER = ProductRepository::mapRow;
	private static final RowMapper<ProductWithBranchResponse> PRODUCT_WITH_BRANCH_MAPPER = ProductRepository::mapProductWithBranchRow;

	private final NamedParameterJdbcTemplate jdbcTemplate;

	public ProductRepository(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Product create(UUID branchId, String name, Integer stock) {
		String sql = "INSERT INTO product (branch_id, name, stock) VALUES (:branchId, :name, :stock) RETURNING id, branch_id, name, stock";
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("branchId", branchId)
				.addValue("name", name)
				.addValue("stock", stock);
		return jdbcTemplate.queryForObject(sql, params, PRODUCT_MAPPER);
	}

	public boolean existsByBranchId(UUID branchId) {
		String sql = "SELECT EXISTS(SELECT 1 FROM branch WHERE id = :branchId)";
		MapSqlParameterSource params = new MapSqlParameterSource("branchId", branchId);
		return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, params, Boolean.class));
	}

	public Product findByIdAndBranchId(UUID productId, UUID branchId) {
		String sql = "SELECT id, branch_id, name, stock FROM product WHERE id = :productId AND branch_id = :branchId";
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("productId", productId)
				.addValue("branchId", branchId);
		try {
			return jdbcTemplate.queryForObject(sql, params, PRODUCT_MAPPER);
		} catch (org.springframework.dao.EmptyResultDataAccessException ex) {
			return null;
		}
	}

	public void delete(UUID productId, UUID branchId) {
		String sql = "DELETE FROM product WHERE id = :productId AND branch_id = :branchId";
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("productId", productId)
				.addValue("branchId", branchId);
		jdbcTemplate.update(sql, params);
	}

	public Product updateStock(UUID productId, UUID branchId, Integer stock) {
		String sql = "UPDATE product SET stock = :stock WHERE id = :productId AND branch_id = :branchId RETURNING id, branch_id, name, stock";
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("productId", productId)
				.addValue("branchId", branchId)
				.addValue("stock", stock);
		try {
			return jdbcTemplate.queryForObject(sql, params, PRODUCT_MAPPER);
		} catch (org.springframework.dao.EmptyResultDataAccessException ex) {
			return null;
		}
	}

	public Product updateName(UUID productId, UUID branchId, String name) {
		String sql = "UPDATE product SET name = :name WHERE id = :productId AND branch_id = :branchId RETURNING id, branch_id, name, stock";
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("productId", productId)
				.addValue("branchId", branchId)
				.addValue("name", name);
		try {
			return jdbcTemplate.queryForObject(sql, params, PRODUCT_MAPPER);
		} catch (org.springframework.dao.EmptyResultDataAccessException ex) {
			return null;
		}
	}

	public List<ProductWithBranchResponse> findMaxStockProductsByFranchise(UUID franchiseId) {
		String sql = """
			SELECT DISTINCT ON (b.id) 
				p.id, 
				b.id AS branch_id, 
				b.name AS branch_name, 
				p.name, 
				p.stock
			FROM product p
			INNER JOIN branch b ON p.branch_id = b.id
			WHERE b.franchise_id = :franchiseId
			ORDER BY b.id, p.stock DESC, p.id
			""";
		MapSqlParameterSource params = new MapSqlParameterSource("franchiseId", franchiseId);
		return jdbcTemplate.query(sql, params, PRODUCT_WITH_BRANCH_MAPPER);
	}

	private static Product mapRow(ResultSet rs, int rowNum) throws SQLException {
		UUID id = rs.getObject("id", UUID.class);
		UUID branchId = rs.getObject("branch_id", UUID.class);
		String name = rs.getString("name");
		Integer stock = rs.getInt("stock");
		return new Product(id, branchId, name, stock);
	}

	private static ProductWithBranchResponse mapProductWithBranchRow(ResultSet rs, int rowNum) throws SQLException {
		UUID id = rs.getObject("id", UUID.class);
		UUID branchId = rs.getObject("branch_id", UUID.class);
		String branchName = rs.getString("branch_name");
		String name = rs.getString("name");
		Integer stock = rs.getInt("stock");
		return new ProductWithBranchResponse(id, branchId, branchName, name, stock);
	}
}

