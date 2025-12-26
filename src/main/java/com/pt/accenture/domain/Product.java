package com.pt.accenture.domain;

import java.util.UUID;

public record Product(UUID id, UUID branchId, String name, Integer stock) {
}

