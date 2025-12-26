package com.pt.accenture.api.dto;

public record ProductUpdateNameRequest(String name) {

	public String normalizedName() {
		return name == null ? "" : name.trim();
	}
}

