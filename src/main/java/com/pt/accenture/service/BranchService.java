package com.pt.accenture.service;

import java.util.UUID;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.pt.accenture.api.dto.BranchCreateRequest;
import com.pt.accenture.api.dto.BranchResponse;
import com.pt.accenture.api.dto.BranchUpdateRequest;
import com.pt.accenture.domain.Branch;
import com.pt.accenture.repository.BranchRepository;

@Service
public class BranchService {

	private final BranchRepository repository;

	public BranchService(BranchRepository repository) {
		this.repository = repository;
	}

	public BranchResponse create(UUID franchiseId, BranchCreateRequest request) {
		if (request == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body requerido");
		}

		if (!repository.existsByFranchiseId(franchiseId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La franquicia no existe");
		}

		String name = request.normalizedName();
		if (name.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
		}
		if (name.length() > 255) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre debe tener hasta 255 caracteres");
		}

		try {
			Branch created = repository.create(franchiseId, name);
			return new BranchResponse(created.id(), created.franchiseId(), created.name());
		}
		catch (DuplicateKeyException ex) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "La sucursal ya existe en esta franquicia", ex);
		}
	}

	public BranchResponse updateName(UUID franchiseId, UUID branchId, BranchUpdateRequest request) {
		if (request == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body requerido");
		}

		if (!repository.existsByFranchiseId(franchiseId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La franquicia no existe");
		}

		Branch existingBranch = repository.findByIdAndFranchiseId(branchId, franchiseId);
		if (existingBranch == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La sucursal no existe en esta franquicia");
		}

		String name = request.normalizedName();
		if (name.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
		}
		if (name.length() > 255) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre debe tener hasta 255 caracteres");
		}

		try {
			Branch updated = repository.updateName(branchId, franchiseId, name);
			if (updated == null) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La sucursal no existe en esta franquicia");
			}
			return new BranchResponse(updated.id(), updated.franchiseId(), updated.name());
		}
		catch (DuplicateKeyException ex) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una sucursal con ese nombre en esta franquicia", ex);
		}
	}
}

