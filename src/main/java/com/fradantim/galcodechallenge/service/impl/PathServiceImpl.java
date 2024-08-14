package com.fradantim.galcodechallenge.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fradantim.galcodechallenge.exception.ElementNotFound;
import com.fradantim.galcodechallenge.model.Path;
import com.fradantim.galcodechallenge.model.Station;
import com.fradantim.galcodechallenge.repository.PathRepository;
import com.fradantim.galcodechallenge.repository.StationRepository;
import com.fradantim.galcodechallenge.service.PathService;

@Service
public class PathServiceImpl implements PathService {

	private final PathRepository pathRepository;
	private final StationRepository stationRepository;

	public PathServiceImpl(PathRepository pathRepository, StationRepository stationRepository) {
		this.pathRepository = pathRepository;
		this.stationRepository = stationRepository;
	}

	@Override
	public void addPath(Long id, Long sourceId, Long destionationId, Double cost) {
		Station source = stationRepository.findById(sourceId)
				.orElseThrow(() -> new ElementNotFound("Station with id " + sourceId + " was not found"));
		Station destination = stationRepository.findById(sourceId)
				.orElseThrow(() -> new ElementNotFound("Station with id " + destionationId + " was not found"));

		Path path = new Path(id, cost, source, destination);
		pathRepository.save(path);

		Optional<Path> previousPath = source.addPath(destination, path);
		destination.addPath(source, path);
		
		previousPath.ifPresent(pathRepository::delete);
	}
}
