package com.pt.accenture.api.dto;

public record FranchiseCreateRequest(String name) {

	public String normalizedName() {
		return name == null ? "" : name.trim();
	}
}
