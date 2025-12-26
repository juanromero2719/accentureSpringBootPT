package com.pt.accenture.service;

import java.util.UUID;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.pt.accenture.api.dto.FranchiseCreateRequest;
import com.pt.accenture.api.dto.FranchiseResponse;
import com.pt.accenture.api.dto.FranchiseUpdateRequest;
import com.pt.accenture.domain.Franchise;
import com.pt.accenture.repository.FranchiseRepository;

@Service
public class FranchiseService {

	private final FranchiseRepository repository;

	public FranchiseService(FranchiseRepository repository) {
		this.repository = repository;
	}

	public FranchiseResponse create(FranchiseCreateRequest request) {
		if (request == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body requerido");
		}

		String name = request.normalizedName();
		if (name.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
		}
		if (name.length() > 255) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre debe tener hasta 255 caracteres");
		}

		try {
			Franchise created = repository.create(name);
			return new FranchiseResponse(created.id(), created.name());
		}
		catch (DuplicateKeyException ex) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "La franquicia ya existe", ex);
		}
	}

	public FranchiseResponse updateName(UUID franchiseId, FranchiseUpdateRequest request) {
		if (request == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body requerido");
		}

		if (!repository.existsById(franchiseId)) {
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
			Franchise updated = repository.updateName(franchiseId, name);
			if (updated == null) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La franquicia no existe");
			}
			return new FranchiseResponse(updated.id(), updated.name());
		}
		catch (DuplicateKeyException ex) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una franquicia con ese nombre", ex);
		}
	}
}
