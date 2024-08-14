package com.fradantim.galcodechallenge.resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fradantim.galcodechallenge.dto.PathDTO;
import com.fradantim.galcodechallenge.service.PathService;

@RestController
@RequestMapping("/paths")
public class PathResource {

	private final PathService pathService;

	public PathResource(PathService pathService) {
		this.pathService = pathService;
	}
	
	@PutMapping("/{path_id}")
	public void addPath(@PathVariable(name = "path_id") Long id, @RequestBody @Validated PathDTO path) {
		pathService.addPath(id, path.sourceId(), path.destionationId(), path.cost());
	}
}
