package com.fradantim.galcodechallenge.resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fradantim.galcodechallenge.dto.StationDTO;
import com.fradantim.galcodechallenge.service.StationService;

@RestController
@RequestMapping("/stations")
public class StationResource {

	private final StationService stationService;

	public StationResource(StationService stationService) {
		this.stationService = stationService;
	}
	
	@PutMapping("/{station_id}")
	public void addStation(@PathVariable(name = "station_id") Long id, @RequestBody StationDTO station) {
		stationService.addStation(id, station.name());
	}
}
