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

import com.fradantim.galcodechallenge.dto.StationDTO;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class StationResourceTests {

	@Value("http://localhost:${local.server.port}")
	private String localUrl;

	@Autowired
	private TestRestTemplate restTemplate;

	public static final AtomicLong nextStationId = new AtomicLong();

	@Test()
	void givenNonExistingStation_whenCreationStation_thenReturnOk() {
		ResponseEntity<Void> response = createStation(nextStationId.incrementAndGet(), UUID.randomUUID().toString());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	private ResponseEntity<Void> createStation(Long id, String name) {
		StationDTO station = new StationDTO(name);
		String url = localUrl + "/stations/" + id;
		RequestEntity<StationDTO> request = RequestEntity.put(url).body(station);
		return restTemplate.exchange(request, Void.class);
	}

	@Test()
	void givenExistingStation_whenCreationStationWithSameId_thenReturnBadRequest() {
		ResponseEntity<Void> response = createStation(nextStationId.incrementAndGet(), UUID.randomUUID().toString());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		response = createStation(nextStationId.get(), UUID.randomUUID().toString());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
}
