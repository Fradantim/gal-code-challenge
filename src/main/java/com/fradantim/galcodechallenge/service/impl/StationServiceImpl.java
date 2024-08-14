package com.fradantim.galcodechallenge.service.impl;

import org.springframework.stereotype.Service;

import com.fradantim.galcodechallenge.model.Station;
import com.fradantim.galcodechallenge.repository.StationRepository;
import com.fradantim.galcodechallenge.service.StationService;

@Service
public class StationServiceImpl implements StationService {

	private final StationRepository stationRepository;

	public StationServiceImpl(StationRepository stationRepository) {
		this.stationRepository = stationRepository;
	}

	@Override
	public void addStation(Long id, String name) {
		stationRepository.save(new Station(id, name));
	}
}
