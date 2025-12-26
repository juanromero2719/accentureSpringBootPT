package com.pt.accenture.api.dto;

public record BranchCreateRequest(String name) {

	public String normalizedName() {
		return name == null ? "" : name.trim();
	}
}

