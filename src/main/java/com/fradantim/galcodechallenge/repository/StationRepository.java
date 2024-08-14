package com.fradantim.galcodechallenge.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.fradantim.galcodechallenge.exception.ElementAlreadyExists;
import com.fradantim.galcodechallenge.model.Station;

public interface StationRepository {

	Optional<Station> findById(Long id);

	Station save(Station station);
}

@Repository
class InMemoryStationRepository implements StationRepository {

	private Map<Long, Station> stations = new ConcurrentHashMap<>();

	@Override
	public Optional<Station> findById(Long id) {
		return Optional.ofNullable(stations.get(id));
	}

	@Override
	public Station save(Station station) {
		// station id is non-null, validated by resource
		Long id = station.id();
		if (stations.containsKey(id))
			throw new ElementAlreadyExists("Station with id " + id + " already exists.");

		stations.put(id, station);
		return station;
	}
}
