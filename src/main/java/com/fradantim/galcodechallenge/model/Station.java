package com.fradantim.galcodechallenge.model;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public record Station(Long id, String name, Map<Station, Path> paths) {
	public Station(Long id, String name) {
		this(id, name, new ConcurrentHashMap<>());
	}

	@Override
	public final boolean equals(Object other) {
		return this == other || other instanceof Station otherStation && Objects.equals(id, otherStation.id);
	}

	@Override
	public final int hashCode() {
		return Objects.hash(id);
	}

	/**
	 * Returns a the replaced <code>this<code> to <code>next</code> Station if
	 * exists.
	 */
	public Optional<Path> addPath(Station next, Path path) {
		return Optional.ofNullable(paths.put(next, path));
	}
}
