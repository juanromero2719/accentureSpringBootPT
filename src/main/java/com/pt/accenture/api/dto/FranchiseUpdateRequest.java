package com.pt.accenture.api.dto;

public record FranchiseUpdateRequest(String name) {

	public String normalizedName() {
		return name == null ? "" : name.trim();
	}
}

