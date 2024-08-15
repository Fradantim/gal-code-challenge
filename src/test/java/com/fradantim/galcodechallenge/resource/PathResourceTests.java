package com.fradantim.galcodechallenge.resource;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
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
import com.fradantim.galcodechallenge.dto.ShortestPathDTO;
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

	@Test()
	void givenNonExistingStations_whenAskingShortestPath_thenReturnNotFound() {
		Long stationA = -1L;
		Long stationB = createRandomStation();

		String url = localUrl + "/paths/" + stationA + "/" + stationB;
		RequestEntity<Void> request = RequestEntity.get(url).build();
		ResponseEntity<Object> response = restTemplate.exchange(request, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test()
	void givenExistingNonLinkedStations_whenAskingShortestPath_thenReturnNotFound() {
		Long stationA = createRandomStation();
		Long stationB = createRandomStation();

		String url = localUrl + "/paths/" + stationA + "/" + stationB;
		RequestEntity<Void> request = RequestEntity.get(url).build();
		ResponseEntity<Object> response = restTemplate.exchange(request, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	void createPath(Long stationA, Long stationB, Double cost) {
		PathDTO path = new PathDTO(cost, stationA, stationB);
		String url = localUrl + "/paths/" + nextPathId.incrementAndGet();
		RequestEntity<PathDTO> request = RequestEntity.put(url).body(path);
		ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test()
	void givenExistingStations_whenAskingShortestPath_thenReturnValue() {
		Long stationA = createRandomStation();
		Long stationB = createRandomStation();
		Long stationC = createRandomStation();
		Long stationD = createRandomStation();

		createPath(stationA, stationB, 50D);
		createPath(stationA, stationC, 100D);
		createPath(stationA, stationD, 60D);
		createPath(stationD, stationC, 20D);

		String url = localUrl + "/paths/" + stationA + "/" + stationD;
		RequestEntity<Void> request = RequestEntity.get(url).build();
		ResponseEntity<ShortestPathDTO> response = restTemplate.exchange(request, ShortestPathDTO.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		ShortestPathDTO path = response.getBody();
		assertThat(path.path()).isEqualTo(List.of(stationA, stationD));
		assertThat(path.cost()).isEqualTo(60D);

		url = localUrl + "/paths/" + stationC + "/" + stationB;
		request = RequestEntity.get(url).build();
		response = restTemplate.exchange(request, ShortestPathDTO.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		path = response.getBody();
		assertThat(path.path()).isEqualTo(List.of(stationC, stationD, stationA, stationB));
		assertThat(path.cost()).isEqualTo(130D);
	}
}
