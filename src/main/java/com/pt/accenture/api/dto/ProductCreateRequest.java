package com.pt.accenture.api.dto;

public record ProductCreateRequest(String name, Integer stock) {

	public String normalizedName() {
		return name == null ? "" : name.trim();
	}
}

