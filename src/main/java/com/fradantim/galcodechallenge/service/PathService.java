package com.fradantim.galcodechallenge.service;

import java.util.Optional;

import com.fradantim.galcodechallenge.dto.ShortestPathDTO;

public interface PathService {

	void addPath(Long id, Long sourceId, Long destionationId, Double cost);
	
	Optional<ShortestPathDTO> findShortestPath(Long sourceId, Long destionationId);
}
