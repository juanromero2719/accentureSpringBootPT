package com.pt.accenture.api.dto;

import java.util.UUID;

public record BranchResponse(UUID id, UUID franchiseId, String name) {
}

