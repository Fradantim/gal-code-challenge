package com.fradantim.galcodechallenge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.annotation.Nonnull;

public record PathDTO(@Nonnull Double cost, @Nonnull @JsonProperty("source_id") Long sourceId,
		@Nonnull @JsonProperty("destination_id") Long destionationId) {
}
