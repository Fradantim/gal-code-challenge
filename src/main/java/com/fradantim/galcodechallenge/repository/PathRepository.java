package com.fradantim.galcodechallenge.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.fradantim.galcodechallenge.exception.ElementAlreadyExists;
import com.fradantim.galcodechallenge.model.Path;

public interface PathRepository {

	Optional<Path> findById(Long id);

	Path save(Path path);
	
	void delete(Path path);
}

@Repository
class InMemoryPathRepository implements PathRepository {

	private Map<Long, Path> paths = new ConcurrentHashMap<>();

	@Override
	public Optional<Path> findById(Long id) {
		return Optional.ofNullable(paths.get(id));
	}

	@Override
	public Path save(Path path) {
		// path id is non-null, validated by resource
		Long id = path.id();
		if (paths.containsKey(id))
			throw new ElementAlreadyExists("Path with id " + id + " already exists.");

		paths.put(id, path);
		return path;
	}
	
	@Override
	public void delete(Path path) {
		paths.remove(path.id());		
	}
}
