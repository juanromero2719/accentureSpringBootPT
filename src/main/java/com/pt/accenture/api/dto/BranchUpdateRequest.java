package com.pt.accenture.api.dto;

public record BranchUpdateRequest(String name) {

	public String normalizedName() {
		return name == null ? "" : name.trim();
	}
}

