package com.fradantim.galcodechallenge.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import com.fradantim.galcodechallenge.dto.ShortestPathDTO;
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

	private Supplier<ElementNotFound> stationNotFound(Long id) {
		return () -> new ElementNotFound("Station with id " + id + " was not found");
	}

	@Override
	public void addPath(Long id, Long sourceId, Long destionationId, Double cost) {
		Station source = stationRepository.findById(sourceId).orElseThrow(stationNotFound(sourceId));
		Station destination = stationRepository.findById(destionationId).orElseThrow(stationNotFound(destionationId));

		Path path = new Path(id, cost, source, destination);
		pathRepository.save(path);

		Optional<Path> previousPath = source.addPath(destination, path);
		destination.addPath(source, path);

		previousPath.ifPresent(pathRepository::delete);
	}

	@Override
	public Optional<ShortestPathDTO> findShortestPath(Long sourceId, Long destionationId) {
		Station source = stationRepository.findById(sourceId).orElseThrow(stationNotFound(sourceId));
		Station destination = stationRepository.findById(destionationId).orElseThrow(stationNotFound(destionationId));
		return recursiveFindShortestPath(destination, source, 0D, List.of(source));
	}

	public Optional<ShortestPathDTO> recursiveFindShortestPath(Station destination, Station source,
			Double accumulatedCost, List<Station> visitedStations) {
		if (source.equals(destination)) {
			List<Long> path = visitedStations.stream().map(Station::id).toList();
			return Optional.of(new ShortestPathDTO(path, accumulatedCost));
		}

		Optional<ShortestPathDTO> bestPath = Optional.empty();
		for (Entry<Station, Path> next : source.paths().entrySet()) {
			Station nextStation = next.getKey();
			if (!visitedStations.contains(nextStation)) {
				Path nextPath = next.getValue();
				// should I test this path?
				if (bestPath.isEmpty() || bestPath.get().cost() > accumulatedCost + nextPath.cost()) {
					List<Station> nextVisitedStations = new ArrayList<>(visitedStations);
					nextVisitedStations.add(nextStation);
					Optional<ShortestPathDTO> thisPathSolution = recursiveFindShortestPath(destination, nextStation,
							accumulatedCost + nextPath.cost(), nextVisitedStations);
					// should I store this result?
					if (bestPath.isEmpty()
							|| thisPathSolution.isPresent() && thisPathSolution.get().cost() < bestPath.get().cost()) {
						bestPath = thisPathSolution;
					}
				}
			}
		}

		return bestPath;
	}
}
