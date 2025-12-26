package com.pt.accenture.api;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pt.accenture.api.dto.BranchCreateRequest;
import com.pt.accenture.api.dto.BranchResponse;
import com.pt.accenture.api.dto.BranchUpdateRequest;
import com.pt.accenture.service.BranchService;

@RestController
@RequestMapping("/franchises")
public class BranchController {

	private final BranchService service;

	public BranchController(BranchService service) {
		this.service = service;
	}

	@PostMapping("/{franchiseId}/branches")
	public ResponseEntity<BranchResponse> create(@PathVariable UUID franchiseId, @RequestBody BranchCreateRequest request) {
		BranchResponse created = service.create(franchiseId, request);
		URI location = URI.create("/franchises/" + franchiseId + "/branches/" + created.id());
		return ResponseEntity.created(location).body(created);
	}

	@PutMapping("/{franchiseId}/branches/{branchId}")
	public ResponseEntity<BranchResponse> updateName(@PathVariable UUID franchiseId, @PathVariable UUID branchId, @RequestBody BranchUpdateRequest request) {
		BranchResponse updated = service.updateName(franchiseId, branchId, request);
		return ResponseEntity.ok(updated);
	}
}

