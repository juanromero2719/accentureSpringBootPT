package com.pt.accenture.service;

import java.util.List;
import java.util.UUID;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.pt.accenture.api.dto.ProductCreateRequest;
import com.pt.accenture.api.dto.ProductResponse;
import com.pt.accenture.api.dto.ProductUpdateNameRequest;
import com.pt.accenture.api.dto.ProductUpdateStockRequest;
import com.pt.accenture.api.dto.ProductWithBranchResponse;
import com.pt.accenture.domain.Product;
import com.pt.accenture.repository.FranchiseRepository;
import com.pt.accenture.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository repository;
	private final FranchiseRepository franchiseRepository;

	public ProductService(ProductRepository repository, FranchiseRepository franchiseRepository) {
		this.repository = repository;
		this.franchiseRepository = franchiseRepository;
	}

	public ProductResponse create(UUID branchId, ProductCreateRequest request) {
		if (request == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body requerido");
		}

		if (!repository.existsByBranchId(branchId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La sucursal no existe");
		}

		String name = request.normalizedName();
		if (name.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
		}
		if (name.length() > 255) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre debe tener hasta 255 caracteres");
		}

		Integer stock = request.stock();
		if (stock == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El stock es obligatorio");
		}
		if (stock < 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El stock no puede ser negativo");
		}

		try {
			Product created = repository.create(branchId, name, stock);
			return new ProductResponse(created.id(), created.branchId(), created.name(), created.stock());
		}
		catch (DuplicateKeyException ex) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "El producto ya existe en esta sucursal", ex);
		}
	}

	public void delete(UUID branchId, UUID productId) {
		if (!repository.existsByBranchId(branchId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La sucursal no existe");
		}

		Product product = repository.findByIdAndBranchId(productId, branchId);
		if (product == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto no existe en esta sucursal");
		}

		repository.delete(productId, branchId);
	}

	public ProductResponse updateStock(UUID branchId, UUID productId, ProductUpdateStockRequest request) {
		if (request == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body requerido");
		}

		if (!repository.existsByBranchId(branchId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La sucursal no existe");
		}

		Product existingProduct = repository.findByIdAndBranchId(productId, branchId);
		if (existingProduct == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto no existe en esta sucursal");
		}

		Integer stock = request.stock();
		if (stock == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El stock es obligatorio");
		}
		if (stock < 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El stock no puede ser negativo");
		}

		Product updated = repository.updateStock(productId, branchId, stock);
		if (updated == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto no existe en esta sucursal");
		}

		return new ProductResponse(updated.id(), updated.branchId(), updated.name(), updated.stock());
	}

	public ProductResponse updateName(UUID branchId, UUID productId, ProductUpdateNameRequest request) {
		if (request == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body requerido");
		}

		if (!repository.existsByBranchId(branchId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La sucursal no existe");
		}

		Product existingProduct = repository.findByIdAndBranchId(productId, branchId);
		if (existingProduct == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto no existe en esta sucursal");
		}

		String name = request.normalizedName();
		if (name.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
		}
		if (name.length() > 255) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre debe tener hasta 255 caracteres");
		}

		try {
			Product updated = repository.updateName(productId, branchId, name);
			if (updated == null) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto no existe en esta sucursal");
			}
			return new ProductResponse(updated.id(), updated.branchId(), updated.name(), updated.stock());
		}
		catch (DuplicateKeyException ex) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un producto con ese nombre en esta sucursal", ex);
		}
	}

	public List<ProductWithBranchResponse> findMaxStockProductsByFranchise(UUID franchiseId) {
		if (!franchiseRepository.existsById(franchiseId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La franquicia no existe");
		}

		return repository.findMaxStockProductsByFranchise(franchiseId);
	}
}

