package com.pt.accenture.domain;

import java.util.UUID;

public record Branch(UUID id, UUID franchiseId, String name) {
}

