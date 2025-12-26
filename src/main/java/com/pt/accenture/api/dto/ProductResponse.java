package com.pt.accenture.api.dto;

import java.util.UUID;

public record ProductResponse(UUID id, UUID branchId, String name, Integer stock) {
}

