package com.pt.accenture.api.dto;

import java.util.UUID;

public record ProductWithBranchResponse(UUID id, UUID branchId, String branchName, String name, Integer stock) {
}

