package com.pt.accenture.api;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pt.accenture.api.dto.FranchiseCreateRequest;
import com.pt.accenture.api.dto.FranchiseResponse;
import com.pt.accenture.api.dto.FranchiseUpdateRequest;
import com.pt.accenture.api.dto.ProductWithBranchResponse;
import com.pt.accenture.service.FranchiseService;
import com.pt.accenture.service.ProductService;

@RestController
@RequestMapping("/franchises")
public class FranchiseController {

	private final FranchiseService service;
	private final ProductService productService;

	public FranchiseController(FranchiseService service, ProductService productService) {
		this.service = service;
		this.productService = productService;
	}

	@GetMapping
	public ResponseEntity<List<FranchiseResponse>> findAll() {
		List<FranchiseResponse> franchises = service.findAll();
		return ResponseEntity.ok(franchises);
	}

	@PostMapping
	public ResponseEntity<FranchiseResponse> create(@RequestBody FranchiseCreateRequest request) {
		FranchiseResponse created = service.create(request);
		URI location = URI.create("/franchises/" + created.id());
		return ResponseEntity.created(location).body(created);
	}

	@GetMapping("/{franchiseId}/products/max-stock")
	public ResponseEntity<List<ProductWithBranchResponse>> getMaxStockProductsByFranchise(@PathVariable UUID franchiseId) {
		List<ProductWithBranchResponse> products = productService.findMaxStockProductsByFranchise(franchiseId);
		return ResponseEntity.ok(products);
	}

	@PutMapping("/{franchiseId}")
	public ResponseEntity<FranchiseResponse> updateName(@PathVariable UUID franchiseId, @RequestBody FranchiseUpdateRequest request) {
		FranchiseResponse updated = service.updateName(franchiseId, request);
		return ResponseEntity.ok(updated);
	}
}
