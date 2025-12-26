package com.pt.accenture.api;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pt.accenture.api.dto.ProductCreateRequest;
import com.pt.accenture.api.dto.ProductResponse;
import com.pt.accenture.api.dto.ProductUpdateNameRequest;
import com.pt.accenture.api.dto.ProductUpdateStockRequest;
import com.pt.accenture.service.ProductService;

@RestController
@RequestMapping("/branches")
public class ProductController {

	private final ProductService service;

	public ProductController(ProductService service) {
		this.service = service;
	}

	@PostMapping("/{branchId}/products")
	public ResponseEntity<ProductResponse> create(@PathVariable UUID branchId, @RequestBody ProductCreateRequest request) {
		ProductResponse created = service.create(branchId, request);
		URI location = URI.create("/branches/" + branchId + "/products/" + created.id());
		return ResponseEntity.created(location).body(created);
	}

	@DeleteMapping("/{branchId}/products/{productId}")
	public ResponseEntity<Void> delete(@PathVariable UUID branchId, @PathVariable UUID productId) {
		service.delete(branchId, productId);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{branchId}/products/{productId}/stock")
	public ResponseEntity<ProductResponse> updateStock(@PathVariable UUID branchId, @PathVariable UUID productId, @RequestBody ProductUpdateStockRequest request) {
		ProductResponse updated = service.updateStock(branchId, productId, request);
		return ResponseEntity.ok(updated);
	}

	@PutMapping("/{branchId}/products/{productId}/name")
	public ResponseEntity<ProductResponse> updateName(@PathVariable UUID branchId, @PathVariable UUID productId, @RequestBody ProductUpdateNameRequest request) {
		ProductResponse updated = service.updateName(branchId, productId, request);
		return ResponseEntity.ok(updated);
	}
}


