package com.fradantim.galcodechallenge.resource;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import com.fradantim.galcodechallenge.dto.PathDTO;
import com.fradantim.galcodechallenge.dto.StationDTO;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PathResourceTests {

	@Value("http://localhost:${local.server.port}")
	private String localUrl;

	@Autowired
	private TestRestTemplate restTemplate;

	public static final AtomicLong nextPathId = new AtomicLong();

	private Long createRandomStation() {
		Long id = StationResourceTests.nextStationId.incrementAndGet();
		StationDTO station = new StationDTO(UUID.randomUUID().toString());
		String url = localUrl + "/stations/" + id;
		RequestEntity<StationDTO> request = RequestEntity.put(url).body(station);
		ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		return id;
	}

	@Test()
	void givenExistingStations_whenCreatingPathWithThose_thenReturnOk() {
		Long stationA = createRandomStation();
		Long stationB = createRandomStation();

		PathDTO path = new PathDTO(0D, stationA, stationB);
		String url = localUrl + "/paths/" + nextPathId.incrementAndGet();
		RequestEntity<PathDTO> request = RequestEntity.put(url).body(path);
		ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	@Test()
	void givenNonExistingStations_whenCreatingPathWithThose_thenReturnNotFound() {
		Long stationA = -1L;
		Long stationB = createRandomStation();

		PathDTO path = new PathDTO(0D, stationA, stationB);
		String url = localUrl + "/paths/" + nextPathId.incrementAndGet();
		RequestEntity<PathDTO> request = RequestEntity.put(url).body(path);
		ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test()
	void givenExistingStations_whenCreatingPathWithExistingId_thenReturnBadRequest() {
		Long stationA = createRandomStation();
		Long stationB = createRandomStation();

		PathDTO path = new PathDTO(0D, stationA, stationB);
		String url = localUrl + "/paths/" + nextPathId.incrementAndGet();
		RequestEntity<PathDTO> request = RequestEntity.put(url).body(path);
		ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		response = restTemplate.exchange(request, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
}
